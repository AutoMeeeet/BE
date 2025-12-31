package com.teamproject.meeting.adapter;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.teamproject.meeting.dto.meeting.MeetingListReqDto;
import com.teamproject.meeting.entity.Meeting;
import com.teamproject.meeting.enums.meeting.MeetingState;
import com.teamproject.meeting.mapper.MeetingMapper;
import com.teamproject.meeting.port.MeetingRepositoryPort;

@Repository
public class MyBatisMeetingRepositoryAdapter implements MeetingRepositoryPort {
	
	private final MeetingMapper meetingMapper;
	
	public MyBatisMeetingRepositoryAdapter(MeetingMapper meetingMapper) {
		this.meetingMapper = meetingMapper;
	}
	
	@Override
	public List<MeetingListReqDto> getMeetings(Long userId, MeetingState state) {
		return meetingMapper.getMeetings(userId, state);
	}
	
	@Override
	public void createMeeting(Meeting meeting) {
		meetingMapper.createMeeting(meeting);
	}
}
