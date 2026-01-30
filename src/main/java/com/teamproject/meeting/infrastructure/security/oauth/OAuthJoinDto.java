package com.teamproject.meeting.infrastructure.security.oauth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OAuthJoinDto {

	private String email;
	private String nickname;
	private String role; // GrantedAuthority에서는 Role 타입을 지원하지 않는다.
}
