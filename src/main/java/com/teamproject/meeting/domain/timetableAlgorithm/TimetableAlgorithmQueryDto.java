package com.teamproject.meeting.domain.timetableAlgorithm;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TimetableAlgorithmQueryDto {
	private Long meetingParticipantId;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
}