package cotato.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cotato.backend.common.dto.BaseResponse;
import cotato.backend.common.dto.DataResponse;
import cotato.backend.dto.MemberDTO.MemberExistenceCheckResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

	@PostMapping("/join")
	@Operation(
		summary = "회원가입",
		description = """
			회원가입한다.
			""",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "성공"
			),
		}
	)
	public ResponseEntity<BaseResponse> memberJoin() {

		return ResponseEntity.ok(BaseResponse.ok());
	}

	@PostMapping("/logout")
	@Operation(
		summary = "로그아웃",
		description = """
			로그아웃한다.

			AccessToken 필요.""",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "성공"
			),
		}
	)
	public ResponseEntity<BaseResponse> memberLogout(HttpServletRequest request) {

		return ResponseEntity.ok(BaseResponse.ok());
	}

	@GetMapping("/duplication")
	@Operation(
		summary = "멤버 중복 체크",
		description = """
			멤버가 존재하는 지 확인한다.

			AccessToken 필요.""",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "성공"
			),
		}
	)
	public ResponseEntity<DataResponse<MemberExistenceCheckResponse>> memberExistenceCheck() {

		MemberExistenceCheckResponse response = new MemberExistenceCheckResponse(true);

		return ResponseEntity.ok(DataResponse.from(response));
	}
}
