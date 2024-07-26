package cotato.backend.domain;

import lombok.Getter;

@Getter
public enum Role {
	USER("USER"),
	GUEST("GUEST"),
	ADMIN("ADMIN"),
	;

	private final String value;

	Role(String value) {
		this.value = value;
	}
}
