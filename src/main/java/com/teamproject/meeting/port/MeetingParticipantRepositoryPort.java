package com.teamproject.meeting.port;

import com.teamproject.meeting.enums.meetingparticipant.Permission;
import com.teamproject.meeting.enums.meetingparticipant.Role;

public interface MeetingParticipantRepositoryPort {

	void createMeeting(
		Long meetingId, 
		Long userId, 
		Boolean emailNotification,
		Role role,
		Permission permission,
		Boolean timetableCase,
		Boolean voteCase
	);
}
