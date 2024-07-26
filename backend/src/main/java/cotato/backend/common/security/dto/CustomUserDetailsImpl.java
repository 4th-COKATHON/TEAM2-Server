package cotato.backend.common.security.dto;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import cotato.backend.domain.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomUserDetailsImpl implements UserDetails {

	private String loginId;
	private String password;
	private String authority;
	private boolean enabled;

	public static CustomUserDetailsImpl of(String loginId, String password, String authority, boolean enabled) {
		return new CustomUserDetailsImpl(loginId, password, authority, enabled);
	}

	public static CustomUserDetailsImpl from(Member member) {
		String loginId = member.getLoginId();
		String password = member.getPassword();
		String authority = member.getRole().getValue();
		boolean enabled = true;

		return new CustomUserDetailsImpl(loginId,password, authority, enabled);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		ArrayList<GrantedAuthority> auth = new ArrayList<>();
		auth.add(new SimpleGrantedAuthority(authority));
		return auth;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return loginId;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}
}
