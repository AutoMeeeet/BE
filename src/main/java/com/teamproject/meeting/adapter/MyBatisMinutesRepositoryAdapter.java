package com.teamproject.meeting.adapter;

import org.springframework.stereotype.Repository;

import com.teamproject.meeting.mapper.MinutesMapper;
import com.teamproject.meeting.port.MinutesRepositoryPort;

@Repository
public class MyBatisMinutesRepositoryAdapter implements MinutesRepositoryPort{
	
	private final MinutesMapper minutesMapper;
	
	public MyBatisMinutesRepositoryAdapter (MinutesMapper minutesMapper) {
		this.minutesMapper = minutesMapper;
	}
	
	@Override
	public void createMeeting(Long meetingId, String minutesUrl) {
		minutesMapper.createMeeting(meetingId, minutesUrl);
	}


}
