package com.teamproject.meeting.infrastructure.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UrlResponse {

	private String message;
	private String token;
	
	public UrlResponse(String message, String token) {
		this.message = message;
		this.token = token;
	}
	
}
