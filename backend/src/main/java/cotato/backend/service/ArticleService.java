package cotato.backend.service;

import cotato.backend.domain.Article;
import cotato.backend.dto.GetNotExpiredArticleResponseDTO;
import cotato.backend.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

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
}