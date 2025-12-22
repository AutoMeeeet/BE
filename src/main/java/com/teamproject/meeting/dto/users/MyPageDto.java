package com.teamproject.meeting.dto.users;

import com.teamproject.meeting.enums.users.Provider;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MyPageDto {
	
	private String nickname;
	private String email;
	private Provider provider;
}