package com.teamproject.meeting.adapter;

import org.springframework.stereotype.Repository;

import com.teamproject.meeting.mapper.MeetingReferenceMapper;
import com.teamproject.meeting.port.MeetingReferenceRepositoryPort;

@Repository
public class MyBatisMeetingReferenceRepositoryAdapter implements MeetingReferenceRepositoryPort{

	private final MeetingReferenceMapper meetingReferenceMapper;
	
	public MyBatisMeetingReferenceRepositoryAdapter(MeetingReferenceMapper meetingReferenceMapper) {
		this.meetingReferenceMapper = meetingReferenceMapper;
	}
	
	@Override
	public void createMeeting(Long meetingId, String referenceUrl) {
		meetingReferenceMapper.createMeeting(meetingId, referenceUrl);
	}
}
