package cotato.backend.common.security.login.handler;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import cotato.backend.common.security.jwt.provider.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException {

		String loginId = extractLoginId((UsernamePasswordAuthenticationToken)authentication);

		String accessToken = jwtTokenProvider.createAccessToken(loginId);
		String refreshToken = jwtTokenProvider.createRefreshToken();

		jwtTokenProvider.sendAccessAndRefreshToken(response, accessToken, refreshToken);
		jwtTokenProvider.updateRefreshToken(loginId, refreshToken);

		log.info("로그인 성공: {}", loginId);
		log.info("accessToken={}", accessToken);
		log.info("refreshToken={}", refreshToken);
	}

	private String extractLoginId(UsernamePasswordAuthenticationToken authentication) {
		return (String)authentication.getPrincipal();
	}

}
