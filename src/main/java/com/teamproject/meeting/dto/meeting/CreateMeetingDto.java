package com.teamproject.meeting.dto.meeting;

import java.time.LocalDateTime;

import com.teamproject.meeting.enums.meeting.LocationType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMeetingDto {
	
	@NotBlank
	private String title;
	@NotNull
	private LocalDateTime startTime;
	@NotNull
	private LocationType locationType;
	@NotBlank
	private String location;
	private String referenceUrl;
	private String minutesUrl;
	@NotNull
	private Integer capacity;
	@NotNull
	private Boolean emailNotification;
}
