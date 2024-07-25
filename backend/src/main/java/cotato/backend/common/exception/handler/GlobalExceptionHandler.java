package cotato.backend.common.exception.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import cotato.backend.common.dto.ErrorResponse;
import cotato.backend.common.exception.CustomException;
import cotato.backend.common.exception.errorCode.ErrorCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<Object> handleCustomException(CustomException e) {
		log.warn("handleCustomException", e);

		return makeErrorResponseEntity(e.getErrorCode());
	}

	// ErrorCode를 받아서 Response를 만드는 메서드
	private ResponseEntity<Object> makeErrorResponseEntity(ErrorCode errorCode) {
		return ResponseEntity
			.status(errorCode.getHttpStatus())
			.body(ErrorResponse.from(errorCode));
	}

	private ResponseEntity<Object> makeErrorResponseEntity(ErrorCode errorCode, String path) {
		return ResponseEntity
			.status(errorCode.getHttpStatus())
			.body(ErrorResponse.of(errorCode, path));
	}
}
