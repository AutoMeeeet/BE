package com.teamproject.meeting.controller.users;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamproject.meeting.dto.users.MyPageDto;
import com.teamproject.meeting.infrastructure.security.local.CustomUserDetails;

@RestController
public class MyPageController {
		
	@GetMapping("/mypage")
	public MyPageDto getMyPage(
			@AuthenticationPrincipal CustomUserDetails user
	) {
		return new MyPageDto(
			user.getNickname(),
			user.getUsername(), // email
			user.getProvider()
		);
	}
}