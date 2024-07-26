package cotato.backend.common.properties.conf;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import cotato.backend.common.properties.JwtProperties;

// 전역적으로 사용되는 상수
@Configuration
@EnableConfigurationProperties(value = {
	JwtProperties.class,
})
public class PropertyConfig {
}
