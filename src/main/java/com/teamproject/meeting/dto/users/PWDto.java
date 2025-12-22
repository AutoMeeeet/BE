package com.teamproject.meeting.dto.users;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PWDto {
	@NotBlank
	String currentPW;
	@NotBlank
	String newPW;
}