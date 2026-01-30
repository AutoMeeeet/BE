package com.teamproject.meeting.dto.users;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginReqDto {
	
	@NotBlank
	private String email;
	
	@NotBlank
	private String password;
}