package cotato.backend.common.security.handler;

import static cotato.backend.common.exception.errorCode.ErrorCode.*;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import cotato.backend.common.dto.ErrorResponse;
import cotato.backend.common.exception.errorCode.ErrorCode;
import cotato.backend.common.util.ResponseWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException, ServletException {
		log.warn("CustomAuthenticationEntryPoint");

		ErrorResponse errorResponse = ErrorResponse.from(UNAUTHORIZED);

		ResponseWriter.writeResponse(response, errorResponse, HttpStatus.UNAUTHORIZED);
	}
}