package com.teamproject.meeting.infrastructure.security.common;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.teamproject.meeting.infrastructure.redis.RedisUtil;
import com.teamproject.meeting.infrastructure.security.local.LoginFilter;
import com.teamproject.meeting.infrastructure.security.oauth.CustomOAuth2UserService;
import com.teamproject.meeting.infrastructure.security.oauth.CustomSuccessHandler;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private final AuthenticationConfiguration authenticationConfiguration;
	private final JWTUtil jwtUtil;
	private final CustomOAuth2UserService customOAuth2UserService;
	private final CustomSuccessHandler customSuccessHandler;
	private final RedisUtil redisUtil;
	
    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil, CustomOAuth2UserService customOAuth2UserService, CustomSuccessHandler customSuccessHandler, RedisUtil redisUtil) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
        this.customOAuth2UserService = customOAuth2UserService;
        this.customSuccessHandler = customSuccessHandler;
        this.redisUtil = redisUtil;
    }
	
	@Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }
	
	// hash Encoder
	@Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http
        	.cors((corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                CorsConfiguration configuration = new CorsConfiguration();

                configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                configuration.setAllowedMethods(Collections.singletonList("*"));
                configuration.setAllowCredentials(true);
                configuration.setAllowedHeaders(Collections.singletonList("*"));
                configuration.setMaxAge(3600L);
                configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
				configuration.setExposedHeaders(Collections.singletonList("Authorization"));
                return configuration;
            }
        })));
	
		// CSRF disable
		http.csrf((auth) -> auth.disable());
		
		// FORM login disable
		http.formLogin((auth) -> auth.disable());

		// HTTP basic disable
		http.httpBasic((auth) -> auth.disable());
		
		// OAuth2
		http
        	.oauth2Login((oauth2) -> oauth2
                .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                        .userService(customOAuth2UserService))
				.successHandler(customSuccessHandler)
		);
		
		// Authorization 설정
		http
        	.authorizeHttpRequests((auth) -> auth
        			.requestMatchers("/login", "/", "/join").permitAll()
        			.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
        			.requestMatchers("/reissue").permitAll()
        			.requestMatchers("/admin/**").hasRole("ADMIN")
        			.anyRequest().authenticated());

		// JWTFilter
		http
        	.addFilterAfter(new JWTFilter(jwtUtil), OAuth2LoginAuthenticationFilter.class);
		
		// UsernamePasswordAuthenticationFilter
		http
        	.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, redisUtil), UsernamePasswordAuthenticationFilter.class);
		
		// logout
		http
        	.addFilterBefore(new CustomLogoutFilter(jwtUtil, redisUtil), LogoutFilter.class);
		
		// Session 설정
		http
        	.sessionManagement((session) -> session
        			.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		
		return http.build();
	}
}
