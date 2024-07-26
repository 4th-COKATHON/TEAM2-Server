package cotato.backend.common.security.handler;

import static cotato.backend.common.exception.errorCode.ErrorCode.*;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import cotato.backend.common.dto.ErrorResponse;
import cotato.backend.common.util.ResponseWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
		AccessDeniedException accessDeniedException) throws IOException, ServletException {
		log.warn("CustomAccessDeniedHandler");

		ErrorResponse errorResponse = ErrorResponse.from(FORBIDDEN);

		ResponseWriter.writeResponse(response, errorResponse, HttpStatus.FORBIDDEN);
	}
}
