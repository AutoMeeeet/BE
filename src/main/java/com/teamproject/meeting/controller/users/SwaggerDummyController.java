package com.teamproject.meeting.controller.users;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.teamproject.meeting.dto.users.LoginReqDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Auth", description = "인증 관련 API (Spring Security Filter 처리)")
@RestController
public class SwaggerDummyController {

	@Operation(summary = "일반 로그인", description = "email과 password를 이용해 로그인")
	@io.swagger.v3.oas.annotations.security.SecurityRequirements
	@PostMapping("/login")
	public void fakeLogin(@RequestBody LoginReqDto loginRequest) {
		
	}
	
	@Operation(summary = "카카오 소셜 로그인", description = "클라이언트에서 이 URL로 브라우저를 이동(Redirect)")
	@io.swagger.v3.oas.annotations.security.SecurityRequirements
	@ApiResponse(responseCode = "302", description = "카카오 로그인 페이지로 이동")
    @GetMapping("/oauth2/authorization/kakao")
    public void fakeKakaoLogin() {
        
    }
	
	@Operation(summary = "구글 소셜 로그인", description = "클라이언트에서 이 URL로 브라우저를 이동(Redirect)")
	@io.swagger.v3.oas.annotations.security.SecurityRequirements
	@ApiResponse(responseCode = "302", description = "구글 로그인 페이지로 이동")
    @GetMapping("/oauth2/authorization/google")
    public void fakeGoogleLogin() {
        
    }
	
	@Operation(summary = "로그아웃", description = "서버 측의 세션/쿠키를 무효화하고 로그아웃")
	@PostMapping("/logout")
	public void fakeLogout() {}
}
