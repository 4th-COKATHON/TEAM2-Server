package cotato.backend.common.redis;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@RedisHash(value = "refreshToken", timeToLive =1209600)
public class RefreshToken {

	@Id
	@Setter
	private String refreshToken;

	@Indexed
	private String loginId;


}
