package com.teamproject.meeting.dto.meetingParticipant;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class VoteResDto {

	private boolean hasVoted; // 투표 여부(애초에 투표를 했는지)
	private List<VoteCandidate> candidates;
	
	@Getter
	@AllArgsConstructor
	public static class VoteCandidate {
		private Long candidateId;
		private LocalDateTime startTime;
		private LocalDateTime endTime;
		private boolean isVote; // 투표 여부(어느 곳에 투표했는지)
	}
}