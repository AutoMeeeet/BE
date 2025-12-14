package com.teamproject.meeting.adapter;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.teamproject.meeting.dto.meeting.MeetingListDto;
import com.teamproject.meeting.enums.meeting.MeetingState;
import com.teamproject.meeting.enums.meeting.MeetingTimeType;
import com.teamproject.meeting.mapper.MeetingMapper;
import com.teamproject.meeting.port.MeetingRepositoryPort;

@Repository
public class MyBatisMeetingRepositoryAdapter implements MeetingRepositoryPort {
	
	private final MeetingMapper meetingMapper;
	
	public MyBatisMeetingRepositoryAdapter(MeetingMapper meetingMapper) {
		this.meetingMapper = meetingMapper;
	}
	
	@Override
	public List<MeetingListDto> getMeetings(Long userId, MeetingState state, MeetingTimeType timeType) {
		return meetingMapper.getMeetings(userId, state, timeType);
	}
}
