package com.teamproject.meeting.adapter;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.teamproject.meeting.domain.timetableAlgorithm.CandidateDto;
import com.teamproject.meeting.dto.meetingParticipant.VoteResultResDto;
import com.teamproject.meeting.mapper.MeetingVoteCandidateMapper;
import com.teamproject.meeting.port.MeetingVoteCandidateRepositoryPort;

@Repository
public class MybatisMeetingVoteCandidateRepositoryAdapter implements MeetingVoteCandidateRepositoryPort{

	private final MeetingVoteCandidateMapper meetingVoteCandidateMapper;
	
	public MybatisMeetingVoteCandidateRepositoryAdapter(MeetingVoteCandidateMapper meetingVoteCandidateMapper) {
		this.meetingVoteCandidateMapper = meetingVoteCandidateMapper;
	}
	
	@Override
	public void createVoteCandidate(Long meetingId, List<CandidateDto> candidates) {
		meetingVoteCandidateMapper.createVoteCandidate(meetingId, candidates);
	}
	
	@Override
	public List<VoteResultResDto.VoteResult> getVoteResult(Long meetingId) {
		return meetingVoteCandidateMapper.getVoteResult(meetingId);
	}
	
	@Override
	public LocalDateTime getStartTime(Long candidateId) {
		return meetingVoteCandidateMapper.getStartTime(candidateId);
	}
}
