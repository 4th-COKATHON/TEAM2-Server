package cotato.backend.common.exception.errorCode;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {

	//4xx
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증에 실패하였습니다."),
	INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "Access Token이 유효하지 않습니다."),
	INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "Refresh Token이 유효하지 않습니다."),
	FORBIDDEN(HttpStatus.FORBIDDEN, "접근이 거부되었습니다."),
	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
	ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 id에 대한 글이 존재하지 않습니다."),
	ARTICLE_NOT_ACCEPTABLE(HttpStatus.NOT_ACCEPTABLE, "해당 글을 조회할 수 있는 기간이 아닙니다."),
	MEMBER_CONFLICT(HttpStatus.CONFLICT, "이미 존재하는 id입니다."),
	SENDER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 발신자입니다."),
	RECEIVER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 수신자입니다."),
	//5xx
	API_INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "API 서버에 문제가 발생하였습니다.")

	;

	private final HttpStatus httpStatus;
	private final String message;

	ErrorCode(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}

	public HttpStatus getHttpStatus() {
		return this.httpStatus;
	}

	public String getMessage() {
		return this.message;
	}

}
