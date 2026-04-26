package com.teamproject.meeting.dto.meetingParticipant;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class VoteResultResDto {
	private List<VoteResult> results;
	
	@Getter
	@AllArgsConstructor
	public static class VoteResult {
		private Long candidateId;
		private LocalDateTime startTime;
		private LocalDateTime endTime;
		private int voteCount;
	}
}
