package cotato.backend.common.util;


import static cotato.backend.common.exception.errorCode.ErrorCode.*;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cotato.backend.common.exception.CustomException;
import cotato.backend.domain.Member;
import cotato.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberLoader {

	private final MemberRepository memberRepository;

	@Transactional
	public Member getMember() {
		String loginId = getLoginId();

		return memberRepository.findByLoginId(loginId)
			.orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
	}

	public String getLoginId() {
		UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken)SecurityContextHolder.getContext()
			.getAuthentication();
		return (String)authentication.getPrincipal();
	}

}
