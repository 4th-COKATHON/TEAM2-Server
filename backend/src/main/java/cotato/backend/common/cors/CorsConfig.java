package cotato.backend.common.cors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import cotato.backend.common.properties.CorsProperties;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class CorsConfig {

	private final CorsProperties properties;

	@Bean
	public UrlBasedCorsConfigurationSource corsConfigurationSources() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(properties.getAllowedOrigins());
		config.setAllowedMethods(properties.getAllowedMethods());
		config.setAllowCredentials(properties.getRequireCredential());
		config.setAllowedHeaders(properties.getAllowedHeaders());
		config.setMaxAge(3600L); //1시간

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		// 허용 url 등록
		properties.getAllowedUrls()
			.forEach(url -> source.registerCorsConfiguration(url, config));

		return source;
	}
}