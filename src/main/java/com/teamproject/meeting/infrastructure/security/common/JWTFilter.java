package com.teamproject.meeting.infrastructure.security.common;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.teamproject.meeting.entity.Users;
import com.teamproject.meeting.infrastructure.security.local.CustomUserDetails;
import com.teamproject.meeting.port.UsersRepositoryPort;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final UsersRepositoryPort usersRepositoryPort;

    public JWTFilter(JWTUtil jwtUtil, UsersRepositoryPort usersRepositoryPort) {
        this.jwtUtil = jwtUtil;
        this.usersRepositoryPort = usersRepositoryPort;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    	String uri = request.getRequestURI();
    	
    	if (uri.equals("/reissue")) {
            filterChain.doFilter(request, response);
            return;
        }
    	
    	String token = extractToken(request);
    	
    	// 토큰이 없다면
    	if (token == null) {
    	    filterChain.doFilter(request, response);
    	    return;
    	}

    	
    	try {
            jwtUtil.isExpired(token);

            String category = jwtUtil.getCategory(token);

            if (!category.equals("access")) {
                sendError(response, "Access token required");
                return;
            }

            authenticateWithToken(token);
            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException ex) { // 토큰 만료
            sendError(response, "ACCESS_TOKEN_EXPIRED");
        } catch (Exception ex) {  // 토큰 검증 실패
            sendError(response, "INVALID_TOKEN");
        }
    	
    }
    
    private String extractToken(HttpServletRequest request) {

        String token = request.getHeader("access");
        if (token != null) {
            return token;
        }

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("access".equals(cookie.getName())) {
                    return cookie.getValue();
                }
                if ("refresh".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }
    	
    private void authenticateWithToken(String token) {
        String email = jwtUtil.getEmail(token);
        
        Users user = usersRepositoryPort.findByEmail(email);

        CustomUserDetails userDetails = new CustomUserDetails(user);

        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);
    }
    
    private void sendError(HttpServletResponse response, String message) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        PrintWriter writer = response.getWriter();
        writer.print("{\"error\":\"" + message + "\"}");
    }
}