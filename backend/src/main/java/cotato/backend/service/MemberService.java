package cotato.backend.service;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cotato.backend.common.exception.CustomException;
import cotato.backend.common.exception.errorCode.ErrorCode;
import cotato.backend.common.redis.LogoutRepository;
import cotato.backend.common.redis.LogoutToken;
import cotato.backend.common.redis.RefreshToken;
import cotato.backend.common.redis.RefreshTokenRepository;
import cotato.backend.domain.Member;
import cotato.backend.domain.Role;
import cotato.backend.dto.MemberDTO.MemberExistenceCheckRequest;
import cotato.backend.dto.MemberDTO.MemberExistenceCheckResponse;
import cotato.backend.dto.MemberDTO.MemberSaveRequest;
import cotato.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

	private final PasswordEncoder passwordEncoder;
	private final RefreshTokenRepository refreshTokenRepository;
	private final LogoutRepository logoutRepository;
	private final MemberRepository memberRepository;

	@Transactional
	public void saveMember(MemberSaveRequest request) {
		String loginId = request.getLoginId();
		String name = request.getName();
		Role role = Role.USER;

		String password = encodePassword(request.getPassword());

		Boolean existence = memberRepository.existsByLoginId(loginId);

		if (existence) {
			throw new CustomException(ErrorCode.MEMBER_CONFLICT);
		}

		Member member = new Member(loginId, password, name, role);
		memberRepository.save(member);
	}

	// 회원 로그아웃 하기
	@Transactional
	public void logoutMember(String loginId, String accessToken) {

		// 회원의 refreshToken 삭제
		RefreshToken refreshToken = refreshTokenRepository.findByLoginId(loginId).orElse(null);
		refreshTokenRepository.deleteById(refreshToken.getRefreshToken());

		// 같은 accessToken으로 다시 로그인하지 못하도록 블랙리스트에 저장
		logoutRepository.save(new LogoutToken(UUID.randomUUID().toString(), accessToken));
	}

	//회원 아이디 존재 확인
	public MemberExistenceCheckResponse checkMemberExistence(MemberExistenceCheckRequest request) {
		String loginId = request.getLoginId();
		Boolean existence = memberRepository.existsByLoginId(loginId);

		return (new MemberExistenceCheckResponse(existence));
	}

	// 패스워드 인코딩
	private String encodePassword(String rawPw) {
		return passwordEncoder.encode(rawPw);
	}

	// 요청된 pw와 Member의 pw가 일치하는 지 확인
	public boolean matchPassword(String rawPw, String pw) {
		return passwordEncoder.matches(rawPw, pw);
	}
}
