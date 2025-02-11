package cotato.backend.common.security.login.service;

import static cotato.backend.common.exception.errorCode.ErrorCode.*;

import java.util.ArrayList;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cotato.backend.common.exception.CustomException;
import cotato.backend.common.exception.errorCode.ErrorCode;
import cotato.backend.common.security.dto.CustomUserDetailsImpl;
import cotato.backend.domain.Member;
import cotato.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

	private final MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Member member = memberRepository.findByLoginId(username).orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

		return CustomUserDetailsImpl.from(member);
	}

}

