package cotato.backend.controller;

import cotato.backend.common.dto.DataResponse;
import cotato.backend.dto.GetNotExpiredArticleResponseDTO;
import cotato.backend.service.ArticleService;
import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cotato.backend.common.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles")
public class ArticleController {

	private final ArticleService articleService;

	@PostMapping
	@Operation(
		summary = "글 저장",
		description = """
			글을 저장한다.
			""",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "성공"
			),
		}
	)
	public ResponseEntity<BaseResponse> articleCreate() {

		return ResponseEntity.ok(BaseResponse.ok());
	}

//	@GetMapping("")
//	@Operation(
//			summary = "타임캡슐 조회",
//			description = """
//                    타임캡슐 조회
//                    3개의 섹션으로 나누어서 조회함""",
//			responses = {
//					@ApiResponse(
//							responseCode = "200",
//							description = "성공"
//					),
//			}
//	)
//	public ResponseEntity<DataResponse<GetNotExpiredArticleResponseDTO>> getTimeCapsule(@PathParam("filter") int filter) {
//		// filter 값에 따라
//		// 1 -> 기간 만료 X, 내가 나한테
//
//		return ResponseEntity.ok(DataResponse.from(articleService.getExpiredArticleService(filter)));
//	}
}
