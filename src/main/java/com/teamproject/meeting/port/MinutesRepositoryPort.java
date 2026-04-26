package com.teamproject.meeting.port;

public interface MinutesRepositoryPort {
	
	void createMeeting(Long meetingId, String MinutesUrl);
}