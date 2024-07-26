package cotato.backend.service;

import static cotato.backend.common.exception.errorCode.ErrorCode.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cotato.backend.common.exception.CustomException;
import cotato.backend.common.exception.errorCode.ErrorCode;
import cotato.backend.domain.Article;
import cotato.backend.domain.Member;
import cotato.backend.dto.ArticleDTO.ArticleGetResponse;
import cotato.backend.dto.ArticleDTO.TimeCapsuleItem;
import cotato.backend.dto.MemberSimpleDTO;
import cotato.backend.dto.PostArticleRequestDTO;
import cotato.backend.dto.PostArticleResponseDTO;
import cotato.backend.dto.SearchNameResponseDTO;
import cotato.backend.repository.ArticleRepository;
import cotato.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ArticleService {

	private final ArticleRepository articleRepository;
	private final MemberRepository memberRepository;
	private final int PAGE_SIZE = 3;

	public Page<TimeCapsuleItem> getTimeCapsules(String loginId, boolean lock, boolean self, int page) {

		Pageable pageable = PageRequest.of(page, PAGE_SIZE);
		LocalDate now = LocalDate.now();

		//1. 아직 잠겨 있으면서, 내가 쓴 글
		//2. 아직 잠겨 있으면서, 남들이 써준 글
		//3. 잠금이 풀렸으면서, 내가 쓴 글
		//4. 잠금이 풀렸으면서, 남들이 써준 글
		Page<Article> articles;
		if (lock && self) {
			articles = articleRepository.findByExpiredAtBeforeAndSender_LoginIdAndReceiver_LoginId(
				now, loginId, loginId,
				pageable);
		} else if (lock && !self) {
			articles = articleRepository.findByExpiredAtBeforeAndSender_LoginId(now, loginId, pageable);
		} else if (!lock && self) {
			articles = articleRepository.findByExpiredAtAfterAndSender_LoginIdAndReceiver_LoginId(now, loginId, loginId,
				pageable);
		} else {
			articles = articleRepository.findByExpiredAtAfterAndSender_LoginId(now, loginId, pageable);
		}

		return articles.map(article -> new TimeCapsuleItem(
			article.getArticleId(),
			article.getTitle(),
			article.getExpiredAt(),
			article.getTitle(),
			calculateDDay(article.getExpiredAt())
		));
	}

    @Transactional
    public PostArticleResponseDTO postArticleService(String loginId, PostArticleRequestDTO postArticleRequestDTO) {
        LocalDateTime now = LocalDateTime.now();

        Member sender = memberRepository.findByLoginId(loginId)
				.orElseThrow(() -> new CustomException(ErrorCode.SENDER_NOT_FOUND));
        Member receiver = memberRepository.findByName(postArticleRequestDTO.receiverName())
				.orElseThrow(() -> new CustomException(ErrorCode.RECEIVER_NOT_FOUND));

        Article article = new Article(null, postArticleRequestDTO.title(), postArticleRequestDTO.detail(), postArticleRequestDTO.expiredAt(), now, sender, receiver);
        articleRepository.save(article);

        return new PostArticleResponseDTO(article.getArticleId());
    }

    @Transactional
    public SearchNameResponseDTO searchNameService(String searchName) {
        if (searchName == null) searchName = "";
        // null인 경우 빈 문자열로 초기화하여 에러 방지함

        List<Member> members = memberRepository.findByNameContaining(searchName);
        List<MemberSimpleDTO> responseMembers = members.stream()
                .map(member -> new MemberSimpleDTO(member.getId(), member.getName()))
                .collect(Collectors.toList());
        return new SearchNameResponseDTO(responseMembers);
    }

	private int calculateDDay(LocalDate expiredAt) {
		// D-Day 계산 로직 구현
		LocalDate now = LocalDate.now();
		return Period.between(now, expiredAt).getDays();
	}

	public ArticleGetResponse getArticle(String memberLoginId, Long articleId) throws CustomException {
		Article article = articleRepository.findById(articleId)
			.orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_FOUND));
		LocalDate expiredAt = article.getExpiredAt();
		LocalDate now = LocalDate.now();

		// 글의 receiver가 본인이 아니라면 에러 반환
		if (!Objects.equals(article.getReceiver().getLoginId(), memberLoginId)) {
			throw new CustomException(FORBIDDEN);
		}

		// 예정시간이 안 지났으면 에러 반환
		if (isBefore(now, expiredAt)) {
			throw new CustomException(ARTICLE_NOT_ACCEPTABLE);
		}

		String senderName = article.getSender().getName();
		Long senderId = article.getSender().getId();
		String receiverName = article.getReceiver().getName();
		String title = article.getTitle();
		String detail = article.getDetail();
		LocalDate createdAt = article.getCreatedAt().toLocalDate();

		return new ArticleGetResponse(senderName, senderId, receiverName, title, detail, createdAt);
	}

	private boolean isBefore(LocalDate a, LocalDate b) {

		return a.isBefore(b);
	}
}