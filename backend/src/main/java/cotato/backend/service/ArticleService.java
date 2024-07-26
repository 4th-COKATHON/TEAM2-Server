package cotato.backend.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cotato.backend.domain.Article;
import cotato.backend.domain.Member;
import cotato.backend.dto.ArticleDTO.TimeCapsuleItem;
import cotato.backend.dto.PostArticleRequestDTO;
import cotato.backend.dto.PostArticleResponseDTO;
import cotato.backend.repository.ArticleRepository;
import cotato.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ArticleService {

	private final ArticleRepository articleRepository;
	private final MemberRepository memberRepository;
	private final int PAGE_SIZE = 3;

	public Page<TimeCapsuleItem> getTimeCapsules(String loginId, boolean lock, boolean self, int page) {

		Pageable pageable = PageRequest.of(page, PAGE_SIZE);
		LocalDateTime now = LocalDateTime.now();

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
			article.getExpiredAt().toLocalDate(),
			article.getTitle(),
			calculateDDay(article.getExpiredAt().toLocalDate())
		));
	}

	public PostArticleResponseDTO postArticleService(String loginId, PostArticleRequestDTO postArticleRequestDTO) {
		LocalDateTime now = LocalDateTime.now();

		Member sender = memberRepository.findById(Long.parseLong(loginId)).orElse(null);
		Member receiver = memberRepository.findByName(postArticleRequestDTO.receiverName());

		Article article = new Article(null, postArticleRequestDTO.title(), postArticleRequestDTO.detail(),
			postArticleRequestDTO.expiredAt(), now, sender, receiver);
		articleRepository.save(article);

		return new PostArticleResponseDTO(article.getArticleId());
	}

	private int calculateDDay(LocalDate expiredAt) {
		// D-Day 계산 로직 구현
		LocalDate now = LocalDate.now();
		return Period.between(now, expiredAt).getDays();
	}
}