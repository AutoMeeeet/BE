package com.teamproject.meeting.infrastructure.security.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class ReissueController {

    private final ReissueService reissueService;

    public ReissueController(ReissueService reissueService) {
        this.reissueService = reissueService;
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

    	String[] refreshData = reissueService.extractRefreshTokenWithUUID(request);
        if (refreshData == null) {
            return new ResponseEntity<>("refresh token not found", HttpStatus.UNAUTHORIZED);
        }

        String refreshToken = refreshData[0];
        String uuid = refreshData[1];
        
        if (!reissueService.validateRefreshToken(refreshToken, uuid)) {
            return new ResponseEntity<>("invalid or expired refresh token", HttpStatus.UNAUTHORIZED);
        }
        
        // 새로운 토큰 발급 및 Redis 갱신
        String[] newTokens = reissueService.reissueTokens(refreshToken, uuid);
        String newAccess = newTokens[0];
        String newRefresh = newTokens[1];
        String newUuid = newTokens[2];
        
        response.setHeader("Authorization", "Bearer " + newAccess);
        response.addCookie(createCookie("refresh", newRefresh, newUuid));

        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    private Cookie createCookie(String key, String value, String uuid) {
    	Cookie cookie = new Cookie(key, value + ":" + uuid);
        cookie.setMaxAge(24 * 60 * 60); // 24시간
        //cookie.setSecure(true); // HTTPS 환경에서 true 설정
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        
        return cookie;
    }
}