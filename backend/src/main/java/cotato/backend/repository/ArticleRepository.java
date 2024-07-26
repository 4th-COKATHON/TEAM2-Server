package cotato.backend.repository;

import cotato.backend.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
//    // 기간이 만료된 글들을 불러옴
//    List<Article> findByExpiredAtBefore(LocalDateTime todayDate);
//
//    // 기간이 지나지 않은 글들을 불러옴
//    List<Article> findByExpiredAtAfter(LocalDateTime todayDate);
//    List<Article> findByExpiredAtAfterAndSenderId(LocalDateTime todayDate, Long senderId);

}