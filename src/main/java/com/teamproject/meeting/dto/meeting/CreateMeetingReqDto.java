package com.teamproject.meeting.dto.meeting;

import java.time.LocalDate;

import com.teamproject.meeting.enums.meeting.LocationType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateMeetingReqDto {
	
	@NotBlank
	private String title;
	
	@NotNull
	private LocalDate startTime;
	
	@NotNull
	@Schema(description = "온라인/오프라인", allowableValues = {"ONLINE", "OFFLINE"})
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
