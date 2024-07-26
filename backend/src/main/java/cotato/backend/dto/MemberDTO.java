package cotato.backend.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class MemberDTO {

	@Getter
	@AllArgsConstructor
	public static class MemberExistenceCheckResponse {
		private Boolean existence;
	}
}