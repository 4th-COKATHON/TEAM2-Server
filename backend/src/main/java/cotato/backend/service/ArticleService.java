package cotato.backend.service;

import cotato.backend.domain.Article;
import cotato.backend.domain.Member;
import cotato.backend.dto.*;
import cotato.backend.repository.ArticleRepository;
import cotato.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

//    // 내가 나에게 작성한 글들 중 기간이 지나지 않은 글들을 불러옴
//    public GetNotExpiredArticleResponseDTO getExpiredArticleService(int filter) {
//        LocalDateTime now = LocalDateTime.now();
//        List<Article> articles = articleRepository.findByExpiredAtAfter(now);
//        // 리턴값 - expiredAt
//
//        if (filter == 1) {
//            return new GetNotExpiredArticleResponseDTO();
//        }
//    }

    public PostArticleResponseDTO postArticleService(String loginId, PostArticleRequestDTO postArticleRequestDTO) {
        LocalDateTime now = LocalDateTime.now();

        Member sender = memberRepository.findById(Long.parseLong(loginId)).orElse(null);
        Member receiver = memberRepository.findByName(postArticleRequestDTO.receiverName());

        Article article = new Article(null, postArticleRequestDTO.title(), postArticleRequestDTO.detail(), postArticleRequestDTO.expiredAt(), now, sender, receiver);
        articleRepository.save(article);

        return new PostArticleResponseDTO(article.getArticleId());
    }

    public SearchNameResponseDTO searchNameService(String searchName) {
        if (searchName == null) searchName = "";
        // null인 경우 빈 문자열로 초기화하여 에러 방지함

        List<Member> members = memberRepository.findByNameContaining(searchName);
        List<MemberSimpleDTO> responseMembers = members.stream()
                .map(member -> new MemberSimpleDTO(member.getId(), member.getName()))
                .collect(Collectors.toList());
        return new SearchNameResponseDTO(responseMembers);
    }
}