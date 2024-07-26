package cotato.backend.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import cotato.backend.domain.Article;
import cotato.backend.domain.Member;

public interface ArticleRepository extends JpaRepository<Article, Long> {
	// 기간이 만료된 글들을 불러옴
	Page<Article> findByExpiredAtBeforeAndSender_LoginId(LocalDateTime todayDate, String senderId, Pageable pageable);

	Page<Article> findByExpiredAtBeforeAndSender_LoginIdAndReceiver_LoginId(LocalDateTime todayDate, String senderId, String receiverId, Pageable pageable);

	// 기간이 지나지 않은 글들을 불러옴
	Page<Article> findByExpiredAtAfterAndSender_LoginId(LocalDateTime todayDate, String senderId, Pageable pageable);

	Page<Article> findByExpiredAtAfterAndSender_LoginIdAndReceiver_LoginId(LocalDateTime todayDate, String senderId, String receiverId, Pageable pageable);

}