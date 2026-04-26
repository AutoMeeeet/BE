package com.teamproject.meeting.port;

import java.util.List;

import com.teamproject.meeting.dto.meetingParticipant.MeetingDetailResDto;

public interface MeetingReferenceRepositoryPort {

	void createMeeting(Long meetingId, String referenceUrl);
	
	List<MeetingDetailResDto.Reference> findByMeetingId(Long meetingId);
	
	Long findMeetingIdByReferenceId(Long referenceId);
	
	void deleteById(Long referenceId);
}
