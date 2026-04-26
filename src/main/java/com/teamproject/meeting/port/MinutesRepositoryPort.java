package com.teamproject.meeting.port;

import java.util.List;

import com.teamproject.meeting.dto.meetingParticipant.MeetingDetailResDto;

public interface MinutesRepositoryPort {
	
	void createMeeting(Long meetingId, String MinutesUrl);
	
	List<MeetingDetailResDto.Minutes> findMeetingId(Long meetingId);
	
	Long findMeetingIdByMinutesId(Long minutesId);
	
	void deleteById(Long minutesId);
}