package com.teamproject.meeting.service.meeting;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamproject.meeting.dto.meeting.CreateMeetingDto;
import com.teamproject.meeting.entity.Meeting;
import com.teamproject.meeting.enums.meeting.MeetingState;
import com.teamproject.meeting.enums.meetingparticipant.Permission;
import com.teamproject.meeting.enums.meetingparticipant.Role;
import com.teamproject.meeting.port.MeetingParticipantRepositoryPort;
import com.teamproject.meeting.port.MeetingReferenceRepositoryPort;
import com.teamproject.meeting.port.MeetingRepositoryPort;
import com.teamproject.meeting.port.MinutesRepositoryPort;

@Service
public class MeetingService {
	
	private final MeetingRepositoryPort meetingRepositoryPort;
	private final MeetingParticipantRepositoryPort meetingParticipantRepositoryPort;
	private final MeetingReferenceRepositoryPort meetingReferenceRepositoryPort;
	private final MinutesRepositoryPort minutesRepositoryPort;
	
	public MeetingService(MeetingRepositoryPort meetingRepositoryPort, MeetingParticipantRepositoryPort meetingParticipantRepositoryPort, MeetingReferenceRepositoryPort meetingReferenceRepositoryPort, MinutesRepositoryPort minutesRepositoryPort) {
		this.meetingRepositoryPort = meetingRepositoryPort;
		this.meetingParticipantRepositoryPort = meetingParticipantRepositoryPort;
		this.meetingReferenceRepositoryPort = meetingReferenceRepositoryPort;
		this.minutesRepositoryPort = minutesRepositoryPort;
	}
	
	@Transactional
	public String createMeeting(Long userId, CreateMeetingDto dto) {
		Meeting meeting = new Meeting();
	    meeting.setTitle(dto.getTitle());
	    meeting.setStartTime(dto.getStartTime());
	    meeting.setLocation(dto.getLocation());
	    meeting.setLocationType(dto.getLocationType());
	    meeting.setCapacity(dto.getCapacity());
	    meeting.setMeetingState(MeetingState.PENDING);
	    meeting.setToken(UUID.randomUUID().toString());
	    meeting.setInviteExpiresAt(LocalDateTime.now().plusDays(7));
	    
	    meetingRepositoryPort.createMeeting(meeting);
	    
	    Long meetingId = meeting.getMeetingId();
	    
		meetingParticipantRepositoryPort.createMeeting(meetingId, userId, dto.getEmailNotification(), Role.ORGANIZER, Permission.AUTHORIZATION, false, false);
		meetingReferenceRepositoryPort.createMeeting(meetingId, dto.getReferenceUrl());
		minutesRepositoryPort.createMeeting(meetingId, dto.getMinutesUrl());
		
		return meeting.getToken();
	}
}
