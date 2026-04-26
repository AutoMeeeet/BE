package com.teamproject.meeting.dto.users;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder // Test용
@AllArgsConstructor // Test용
public class PwReqDto {
	
	@NotBlank
	String currentPW;
	
	@NotBlank
	String newPW;
}