package com.teamproject.meeting.dto.users;

import com.teamproject.meeting.enums.users.Provider;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MyPageResDto {
	
	private String nickname;
	
	private String email;
	
	@Schema(description = "인증의 주체", allowableValues = {"KAKAO", "GOOGLE", "LOCAL"})
	private Provider provider;
}