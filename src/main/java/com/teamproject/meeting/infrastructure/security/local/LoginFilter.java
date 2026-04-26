package com.teamproject.meeting.infrastructure.security.local;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teamproject.meeting.infrastructure.redis.RedisUtil;
import com.teamproject.meeting.infrastructure.security.common.JWTUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final RedisUtil redisUtil;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, RedisUtil redisUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.redisUtil = redisUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
        	ObjectMapper objectMapper = new ObjectMapper(); // JSON 데이터 읽기
            Map<String, String> loginData = objectMapper.readValue(request.getInputStream(), Map.class);

            // JSON에서 username과 password 추출
            String email = loginData.get("email");
            String password = loginData.get("password");
    		
    		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);

    		return authenticationManager.authenticate(authToken);
    		} catch (IOException e) {
    	        throw new AuthenticationServiceException("Error parsing login request");
    	    }
    	}

	//로그인 성공시
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {

    	//유저 정보
        String email = authentication.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        //토큰 생성
        String accessToken = jwtUtil.createJwt("access", email, role, 600_000L); // 10분
        String refreshToken = jwtUtil.createJwt("refresh", email, role, 86_400_000L); // 1일

        String uuid = java.util.UUID.randomUUID().toString();
        
        redisUtil.saveRefreshToken(email, uuid, refreshToken, 86_400_000L);
        
        //응답 설정
        response.setHeader("Authorization", "Bearer " + accessToken);
        response.addCookie(createCookie("refresh", refreshToken, uuid));
        response.setStatus(HttpStatus.OK.value());
    }

	//로그인 실패시
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {

    	response.setStatus(401);
    }
    
    private Cookie createCookie(String key, String value, String uuid) {

        Cookie cookie = new Cookie(key, value + ":" + uuid);
        cookie.setMaxAge(24*60*60); // 1일
        //cookie.setSecure(true);
        cookie.setPath("/"); // 적용 범위
        cookie.setHttpOnly(true); // JS로 내부 접근 불가

        return cookie;
    }
}
