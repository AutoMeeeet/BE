package com.teamproject.meeting.dto.users;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinDto {
	
	private String email;
	private String password;
	private String nickname;
}