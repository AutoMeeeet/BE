package com.teamproject.meeting.dto.meetingParticipant;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateAvailabilityReqDto {
	
	@NotNull
	private final LocalDateTime startTime;
	
	@NotNull
	private final LocalDateTime endTime;
	
	@JsonCreator
	public CreateAvailabilityReqDto(
		@JsonProperty("startTime") LocalDateTime startTime,
		@JsonProperty("endTime") LocalDateTime endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}
}