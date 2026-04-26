package com.teamproject.meeting.adapter;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.teamproject.meeting.dto.meetingParticipant.MeetingDetailResDto;
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
	
	@Override
	public List<MeetingDetailResDto.Reference> findByMeetingId(Long meetingId) {
		return meetingReferenceMapper.findByMeetingId(meetingId);
	}
	
	@Override
	public Long findMeetingIdByReferenceId(Long referenceId) {
		return meetingReferenceMapper.findMeetingIdByReferenceId(referenceId);
	}
	
	@Override
	public void deleteById(Long referenceId) {
		meetingReferenceMapper.deleteById(referenceId);
	}
}
