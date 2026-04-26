package com.teamproject.meeting.adapter;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.teamproject.meeting.dto.meetingParticipant.MeetingDetailResDto;
import com.teamproject.meeting.dto.meetingParticipant.PermissionUpdateReqDto;
import com.teamproject.meeting.dto.meetingParticipant.TimetableQueryDto;
import com.teamproject.meeting.dto.meetingParticipant.VoteResDto;
import com.teamproject.meeting.entity.MeetingAvailability;
import com.teamproject.meeting.enums.meetingparticipant.Permission;
import com.teamproject.meeting.enums.meetingparticipant.Role;
import com.teamproject.meeting.mapper.MeetingParticipantMapper;
import com.teamproject.meeting.port.MeetingParticipantRepositoryPort;

@Repository
public class MyBatisMeetingParticipantRepositoryAdapter implements MeetingParticipantRepositoryPort{

	private final MeetingParticipantMapper meetingParticipantMapper;
	
	public MyBatisMeetingParticipantRepositoryAdapter(MeetingParticipantMapper meetingParticipantMapper) {
		this.meetingParticipantMapper = meetingParticipantMapper;
	}
	
	@Override
	public void createMeeting(
		Long meetingId, 
		Long userId, 
		Boolean emailNotification,
		Role role,
		Permission permission,
		Boolean timetableCase,
		Boolean voteCase
		) {
		meetingParticipantMapper.createMeeting(
			meetingId, 
			userId, 
			emailNotification,
			role,
			permission,
			timetableCase,
			voteCase
		);
	}
	
	@Override
	public List<TimetableQueryDto> getTimetable(Long meetingId) {
		return meetingParticipantMapper.getTimetable(meetingId);
	}
	
	@Override
	public Long findParticipantId(Long meetingId, Long userId) {
		return meetingParticipantMapper.findParticipantId(meetingId, userId);
	}
	
	@Override
	public int confirmTimetable(Long participantId) {
		return meetingParticipantMapper.confirmTimetable(participantId);
	}
	
	@Override
	public int countUnconfirmedTimetable(Long meetingId) {
		return meetingParticipantMapper.countUnconfirmedTimetable(meetingId);
	}
	
	@Override
	public List<VoteResDto.VoteCandidate> getVoteCandidates(Long meetingId, Long userId) {
		return meetingParticipantMapper.getVoteCandidates(meetingId, userId);
	}
	
	@Override
	public void createVoteRecord(Long userId, Long meetingId, Long candidateId) {
		meetingParticipantMapper.createVoteRecord(userId, meetingId, candidateId);
	}
	
	@Override
	public int countUnvotedParticipant(Long meetingId) {
		return meetingParticipantMapper.countUnvotedParticipant(meetingId);
	}
	
	@Override
	public boolean isOrganizer(Long meetingId, Long userId) {
		return meetingParticipantMapper.isOrganizer(meetingId, userId);
	}
	
	@Override
	public boolean existByMeetingIdAndUserId(Long meetingId, Long userId) {
		return meetingParticipantMapper.existByMeetingIdAndUserId(meetingId, userId);
	}
	
	@Override
	public List<MeetingDetailResDto.Participant> findParticipants(Long meetingId, Long userId) {
		return meetingParticipantMapper.findParticipants(meetingId, userId);
	}
	
	@Override
	public void createAvailability(MeetingAvailability availability) {
		meetingParticipantMapper.createAvailability(availability);
	}
	
	@Override
	public void deleteAvailability(Long participantId, Long availabilityId) {
		meetingParticipantMapper.deleteAvailability(participantId, availabilityId);
	}
	
	@Override
	public String findPermission(Long meetingId, Long userId) {
		return meetingParticipantMapper.findPermission(meetingId, userId);
	}
	
	@Override
	public void updatePermission(Long meetingId, Long targetUserId, PermissionUpdateReqDto dto) {
		meetingParticipantMapper.updatePermission(meetingId, targetUserId, dto);
	}
	
	@Override
	public void deleteParticipant(Long participantId) {
		meetingParticipantMapper.deleteParticipant(participantId);
	}
}
