package com.teamproject.meeting.infrastructure.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResponse {
	private String message;
	
	public CommonResponse(String message) {
		this.message = message;
	}
}
