package com.teamproject.meeting.adapter;

import org.springframework.stereotype.Repository;

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
}
