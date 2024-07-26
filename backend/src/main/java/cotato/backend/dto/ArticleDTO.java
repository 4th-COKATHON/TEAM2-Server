package cotato.backend.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class ArticleDTO {

	@Getter
	@AllArgsConstructor
	public static class TimeCapsulesGetResponse {
		private List<TimeCapsuleItem> timeCapsules;

	}

	@Getter
	@AllArgsConstructor
	public static class TimeCapsuleItem {
		private Long articleId;
		private String name;

		@JsonFormat(pattern = "yyyy-MM-dd")
		private LocalDate date;
		private String title;
		private int dDay;
	}

	@Getter
	@AllArgsConstructor
	public static class ArticleGetResponse {
		private String senderName;
		private Long senderId;
		private String receiverName;

		private String title;
		private String detail;

		@JsonFormat(pattern = "yyyy-MM-dd")
		private LocalDate date;

	}
}
