package cotato.backend.domain;

import lombok.Getter;

@Getter
public enum Role {
	USER("ROLE_USER"),
	GUEST("ROLE_GUEST"),
	ADMIN("ROLE_ADMIN"),
	;

	private final String value;

	Role(String value) {
		this.value = value;
	}
}
