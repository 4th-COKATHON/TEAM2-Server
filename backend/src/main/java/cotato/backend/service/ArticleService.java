package cotato.backend.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cotato.backend.dto.ArticleDTO.TimeCapsuleItem;
import cotato.backend.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ArticleService {

	private final int PAGE_SIZE = 3;

	private final ArticleRepository articleRepository;

	public Page<TimeCapsuleItem> getTimeCapsules(String loginId, boolean lock, boolean self, int page) {

		Pageable pageable = PageRequest.of(page, PAGE_SIZE);
		LocalDateTime now = LocalDateTime.now();

		//1. 아직 잠겨 있으면서, 내가 쓴 글
		//2. 아직 잠겨 있으면서, 남들이 써준 글
		//3. 잠금이 풀렸으면서, 내가 쓴 글
		//4. 잠금이 풀렸으면서, 남들이 써준 글
		if (lock && self) {
			articleRepository.findByExpiredAtBeforeAndSender_LoginIdAndReceiver_LoginId(now, loginId, loginId,
				pageable);
		} else if (lock && !self) {
			articleRepository.findByExpiredAtBeforeAndSender_LoginId(now, loginId, pageable);
		} else if (!lock && self) {
			articleRepository.findByExpiredAtAfterAndSender_LoginIdAndReceiver_LoginId(now, loginId, loginId, pageable);
		} else {
			articleRepository.findByExpiredAtAfterAndSender_LoginId(now, loginId, pageable);
		}

		List<TimeCapsuleItem> objects = new ArrayList<>();

		return new PageImpl<>(objects, pageable, 1);
	}

}