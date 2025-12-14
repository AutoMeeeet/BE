package com.teamproject.meeting.service.meeting;

import java.util.List;

import org.springframework.stereotype.Service;

import com.teamproject.meeting.dto.meeting.MeetingListDto;
import com.teamproject.meeting.enums.meeting.MeetingState;
import com.teamproject.meeting.enums.meeting.MeetingTimeType;
import com.teamproject.meeting.port.MeetingRepositoryPort;

@Service
public class MeetingListService {
	
	private final MeetingRepositoryPort meetingRepositoryPort;
	
	public MeetingListService(MeetingRepositoryPort meetingRepositoryPort) {
		this.meetingRepositoryPort = meetingRepositoryPort;
	}
	
	public List<MeetingListDto> getMeetings(
			Long userId,
			MeetingState state,
			MeetingTimeType timeType
	) {
		if(timeType == null) {
			timeType = MeetingTimeType.UPCOMING;
		}
		
		return meetingRepositoryPort.getMeetings(userId, state, timeType);
	}
}
