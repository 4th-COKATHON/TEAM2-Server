package cotato.backend.common.security.config;

import java.util.stream.Stream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fasterxml.jackson.databind.ObjectMapper;

import cotato.backend.common.properties.SecurityProperties;
import cotato.backend.common.security.handler.CustomAccessDeniedHandler;
import cotato.backend.common.security.handler.CustomAuthenticationEntryPoint;
import cotato.backend.common.security.handler.ExceptionHandlingFilter;
import cotato.backend.common.security.jwt.filter.JwtAuthenticationFilter;
import cotato.backend.common.security.jwt.provider.JwtTokenProvider;
import cotato.backend.common.security.login.filter.JsonNamePasswordAuthenticationFilter;
import cotato.backend.common.security.login.handler.LoginFailureHandler;
import cotato.backend.common.security.login.handler.LoginSuccessHandler;
import cotato.backend.common.security.login.service.LoginService;
import cotato.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

	private final ObjectMapper objectMapper;
	private final JwtTokenProvider jwtTokenProvider;
	private final MemberRepository memberRepository;

	private final SecurityProperties securityProperties;
	private final UrlBasedCorsConfigurationSource ConfigurationSource;

	private final LoginSuccessHandler loginSuccessHandler;
	private final LoginFailureHandler loginFailureHandler;

	private final AntPathMatcher pathMatcher = new AntPathMatcher();
	private final PasswordEncoderConfig passwordEncoderConfig;
	private final LoginService loginService;


	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable) // 세션을 사용안하므로 csrf 공격 없으므로 csrf 비활성화
			.httpBasic(AbstractHttpConfigurer::disable) // 기본 인증 방식 비활성화
			.cors(cors -> cors.configurationSource(ConfigurationSource))
			.formLogin(AbstractHttpConfigurer::disable) //json을 이용하여 로그인을 하므로 기본 Login 비활성화
			.authorizeHttpRequests((authorize) -> authorize // PERMIT_URLS만 바로 접근 가능, 나머지 URL은 인증 필요
				.requestMatchers(securityProperties.getPermitUrls()).permitAll()
				.requestMatchers(securityProperties.getAuthorizationRequiredUrls()).hasRole("USER")
				.anyRequest().authenticated()
			)
			.logout(LogoutConfigurer::disable) // 로그아웃 비활성화
			.sessionManagement(session -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // 토큰을 사용하기 때문에 Session 사용 x
		;

		//필터 체인 추가
		http
			.addFilterAfter(jsonNamePasswordAuthenticationFilter(), LogoutFilter.class)
			.addFilterBefore(jwtAuthenticationFilter(), JsonNamePasswordAuthenticationFilter.class)
			.addFilterBefore(exceptionHandlingFilter(), JwtAuthenticationFilter.class);

		//예외 처리 추가
		http
			.exceptionHandling(
				httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(
					new CustomAuthenticationEntryPoint())
			)
			.exceptionHandling(
				httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer.accessDeniedHandler(
					new CustomAccessDeniedHandler())
			);

		return http.build();
	}

	//authentication Provider 관리를 위한 Manager 등록
	@Bean
	public AuthenticationManager authenticationManager() throws Exception {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

		provider.setUserDetailsService(loginService);
		provider.setPasswordEncoder(passwordEncoderConfig.passwordEncoder());

		return new ProviderManager(provider);
	}

	//로그인 필터 등록
	@Bean
	public JsonNamePasswordAuthenticationFilter jsonNamePasswordAuthenticationFilter() throws Exception {
		JsonNamePasswordAuthenticationFilter jsonUsernamePasswordLoginFilter = new JsonNamePasswordAuthenticationFilter(
			objectMapper);
		jsonUsernamePasswordLoginFilter.setAuthenticationManager(authenticationManager());

		jsonUsernamePasswordLoginFilter.setAuthenticationSuccessHandler(loginSuccessHandler);
		jsonUsernamePasswordLoginFilter.setAuthenticationFailureHandler(loginFailureHandler);

		return jsonUsernamePasswordLoginFilter;
	}

	//JWT 필터 등록
	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter(jwtTokenProvider, memberRepository, securityProperties, pathMatcher);
	}

	//예외 핸들링 필터 등록
	@Bean
	public ExceptionHandlingFilter exceptionHandlingFilter() {
		return new ExceptionHandlingFilter();
	}

}

