package com.teamproject.meeting.domain.timetableAlgorithm;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CandidateDto {

	private LocalDateTime startTime;
	private LocalDateTime endTime;
}
