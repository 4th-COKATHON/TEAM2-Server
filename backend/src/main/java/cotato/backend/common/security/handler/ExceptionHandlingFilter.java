package cotato.backend.common.security.handler;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import cotato.backend.common.dto.ErrorResponse;
import cotato.backend.common.exception.CustomException;
import cotato.backend.common.util.ResponseWriter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExceptionHandlingFilter extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (CustomException e) {
			log.warn("ExceptionHandlingFilter: {}", e.getMessage());

			ErrorResponse errorResponse = ErrorResponse.from(e.getErrorCode());
			ResponseWriter.writeResponse(response, errorResponse, e.getErrorCode().getHttpStatus());
		} catch (Exception e) {
			log.warn("ExceptionHandlingFilter: {}", e.getMessage());

			ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
			ResponseWriter.writeResponse(response, errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
