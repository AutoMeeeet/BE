package com.teamproject.meeting.port;

public interface MeetingReferenceRepositoryPort {

	void createMeeting(Long meetingId, String referenceUrl);
	
}
