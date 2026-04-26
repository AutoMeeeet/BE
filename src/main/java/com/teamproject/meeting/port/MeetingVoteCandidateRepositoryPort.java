package com.teamproject.meeting.port;

import java.time.LocalDateTime;
import java.util.List;

import com.teamproject.meeting.domain.timetableAlgorithm.CandidateDto;
import com.teamproject.meeting.dto.meetingParticipant.VoteResultResDto;

public interface MeetingVoteCandidateRepositoryPort {
	
	void createVoteCandidate(Long meetingId, List<CandidateDto> candidates);
	
	List<VoteResultResDto.VoteResult> getVoteResult(Long meetingId);
	
	LocalDateTime getStartTime(Long candidateId);
}
