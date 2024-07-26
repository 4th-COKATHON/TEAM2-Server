package cotato.backend.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cotato.backend.common.dto.DataResponse;
import cotato.backend.common.dto.ErrorResponse;
import cotato.backend.common.util.MemberLoader;
import cotato.backend.domain.Member;
import cotato.backend.dto.ArticleDTO.ArticleGetResponse;
import cotato.backend.dto.ArticleDTO.TimeCapsuleItem;
import cotato.backend.dto.PostArticleRequestDTO;
import cotato.backend.dto.PostArticleResponseDTO;
import cotato.backend.dto.SearchNameResponseDTO;
import cotato.backend.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles")
public class ArticleController {

	private final ArticleService articleService;
	private final MemberLoader memberLoader;

	@PostMapping
	@Operation(
		summary = "글 저장",
		description = """
			글을 저장한다.
						
			accessToken 필요.
			""",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "성공"
			),
		}
	)
	public ResponseEntity<DataResponse<PostArticleResponseDTO>> articleCreate(
		@RequestBody PostArticleRequestDTO postArticleRequestDTO) {

		Member member = memberLoader.getMember();
		String loginId = memberLoader.getLoginId();

		PostArticleResponseDTO response = articleService.postArticleService(loginId, postArticleRequestDTO);

		return ResponseEntity.ok(DataResponse.from(response));
	}

	@GetMapping("/search")
	@Operation(
		summary = "닉네임 검색",
		description = """
			닉네임을 검색한다.
						
			accessToken 필요.
			""",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "성공"
			),
		}
	)
	public ResponseEntity<DataResponse<SearchNameResponseDTO>> searchName(
		@RequestParam("searchName") String searchName) {
		SearchNameResponseDTO response = articleService.searchNameService(searchName);

		return ResponseEntity.ok(DataResponse.from(response));
	}

	@GetMapping
	@Operation(
		summary = "타임캡슐 조회",
		description = """
			타임캡슐 조회
						
			쿼리 파라미터로는 lock = [true, false], self = [true, false]가 올 수 있다.
						
			accessToken 필요.
			""",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "성공"
			),
			@ApiResponse(
				responseCode = "400",
				description = "요청 파라미터가 잘 못 되었습니다.",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))
			),
		}
	)
	public ResponseEntity<DataResponse<Page<TimeCapsuleItem>>> timeCapsulesGet(
		@PathParam("lock") boolean lock,
		@PathParam("self") boolean self,
		@PathParam("page") int page
	) {
		String loginId = memberLoader.getLoginId();

		Page<TimeCapsuleItem> timeCapsules = articleService.getTimeCapsules(loginId, lock, self, page);

		return ResponseEntity.ok(DataResponse.from(timeCapsules));
	}

	@GetMapping("/{articleId}")
	@Operation(
		summary = "글 상세 조회",
		description = """
			글을 상세조회한다.
						
			accessToken 필요.
			""",
		responses = {
			@ApiResponse(
				responseCode = "200",
				description = "성공"
			),
			@ApiResponse(
				responseCode = "403",
				description = "timeCapsule을 조회할 수 있는 권한이 없습니다.",
				content = @Content(schema = @Schema(implementation = ErrorResponse.class))
			),
		}
	)
	public ResponseEntity<DataResponse<ArticleGetResponse>> articleGet(@PathParam("articleId") Long articleId) {
		String loginId = memberLoader.getLoginId();

		ArticleGetResponse response = articleService.getArticle(loginId, articleId);

		return ResponseEntity.ok(DataResponse.from(response));
	}

}
