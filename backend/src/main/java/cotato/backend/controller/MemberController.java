package cotato.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cotato.backend.common.dto.BaseResponse;
import cotato.backend.common.dto.DataResponse;
import cotato.backend.common.dto.ErrorResponse;
import cotato.backend.common.security.jwt.provider.JwtTokenProvider;
import cotato.backend.common.util.MemberLoader;
import cotato.backend.dto.MemberDTO;
import cotato.backend.dto.MemberDTO.MemberExistenceCheckResponse;
import cotato.backend.dto.MemberDTO.MemberSaveRequest;
import cotato.backend.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

	private final MemberService memberService;
	private final MemberLoader memberLoader;
	private final JwtTokenProvider jwtTokenProvider;

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
			@ApiResponse(
				responseCode = "409",
				description = "이미 존재하는 id입니다.",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))
			)
		}
	)
	public ResponseEntity<BaseResponse> memberSave(@RequestBody MemberSaveRequest request) {
		memberService.saveMember(request);

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

		String accessToken = jwtTokenProvider.extractAccessToken(request).orElse(null);

		String loginId = memberLoader.getLoginId();

		memberService.logoutMember(loginId, accessToken);

		return ResponseEntity.ok(BaseResponse.ok());
	}

	@GetMapping("/existence")
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
	public ResponseEntity<DataResponse<MemberExistenceCheckResponse>> memberExistenceCheck(@RequestBody
		MemberDTO.MemberExistenceCheckRequest request) {

		MemberExistenceCheckResponse response = memberService.checkMemberExistence(request);

		return ResponseEntity.ok(DataResponse.from(response));
	}
}
