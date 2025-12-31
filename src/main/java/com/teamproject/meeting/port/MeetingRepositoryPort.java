package com.teamproject.meeting.port;

import java.util.List;

import com.teamproject.meeting.dto.meeting.MeetingListReqDto;
import com.teamproject.meeting.entity.Meeting;
import com.teamproject.meeting.enums.meeting.MeetingState;

public interface MeetingRepositoryPort {
	
	List<MeetingListReqDto> getMeetings(Long userId, MeetingState state);
	
	void createMeeting(Meeting meeting);
}