package com.teamproject.meeting.adapter;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.teamproject.meeting.dto.meetingParticipant.MeetingDetailResDto;
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
	
	@Override
	public List<MeetingDetailResDto.Minutes> findMeetingId(Long meetingId) {
		return minutesMapper.findMeetingId(meetingId);
	}
	
	@Override
	public Long findMeetingIdByMinutesId(Long minutesId) {
		return minutesMapper.findMeetingIdByMinutesId(minutesId);
	}
	
	@Override
	public void deleteById(Long minutesId) {
		minutesMapper.deleteById(minutesId);
	}
}
