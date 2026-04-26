package com.teamproject.meeting.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.teamproject.meeting.domain.timetableAlgorithm.CandidateDto;
import com.teamproject.meeting.dto.meetingParticipant.VoteResultResDto;

@Mapper
public interface MeetingVoteCandidateMapper {

	void createVoteCandidate(
		@Param("meetingId") Long meetingId,	
		@Param("candidates") List<CandidateDto> candidates
	);
	
	List<VoteResultResDto.VoteResult> getVoteResult(@Param("meetingId") Long meetingId);
	
	LocalDateTime getStartTime(@Param("candidateId") Long candidateId);
}
