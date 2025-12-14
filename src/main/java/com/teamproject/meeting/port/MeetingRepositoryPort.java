package com.teamproject.meeting.port;

import java.util.List;

import com.teamproject.meeting.dto.meeting.MeetingListDto;
import com.teamproject.meeting.enums.meeting.MeetingState;
import com.teamproject.meeting.enums.meeting.MeetingTimeType;

public interface MeetingRepositoryPort {
	List<MeetingListDto> getMeetings(Long userId, MeetingState state, MeetingTimeType timeType);
}