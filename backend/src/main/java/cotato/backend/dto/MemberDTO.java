package cotato.backend.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberDTO {

	@Getter
	@AllArgsConstructor
	public static class MemberExistenceCheckResponse {
		private Boolean existence;
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class MemberSaveRequest {

		private String loginId;
		private String name;
		private String password;
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class MemberExistenceCheckRequest {

		private String loginId;
	}

	@Getter
	@AllArgsConstructor
	public static class MemberSearchResponse {
		private Long memberId;
		private String name;
	}
}