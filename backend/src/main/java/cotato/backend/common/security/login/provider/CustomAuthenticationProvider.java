package cotato.backend.common.security.login.provider;

import java.util.Collection;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import cotato.backend.common.security.login.service.LoginService;
import cotato.backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

	private final LoginService userDetailsService;
	private final MemberService memberService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String loginId = (String)authentication.getPrincipal();
		String password = (String)authentication.getCredentials();

		UserDetails user = userDetailsService.loadUserByUsername(loginId);

		// 비밀번호 확인
		if (!memberService.matchPassword(password, user.getPassword())) {
			throw new BadCredentialsException("비밀번호가 틀렸습니다.");
		}

		Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>)user.getAuthorities();

		return UsernamePasswordAuthenticationToken.authenticated(loginId, password, authorities);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}
}
