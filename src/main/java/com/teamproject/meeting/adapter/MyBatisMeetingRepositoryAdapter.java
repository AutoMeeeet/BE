package com.teamproject.meeting.adapter;

import java.util.List;
import java.time.LocalDateTime;

import org.springframework.stereotype.Repository;

import com.teamproject.meeting.domain.timetableAlgorithm.TimetableAlgorithmQueryDto;
import com.teamproject.meeting.dto.meeting.MainPageResDto;
import com.teamproject.meeting.dto.meeting.MeetingListReqDBDto;
import com.teamproject.meeting.dto.meetingParticipant.MeetingDetailResDto;
import com.teamproject.meeting.dto.meetingParticipant.MeetingUpdateReqDto;
import com.teamproject.meeting.dto.meetingParticipant.StepResDto;
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
	public List<MeetingListReqDBDto> getMeetings(Long userId, MeetingState state) {
		return meetingMapper.getMeetings(userId, state);
	}
	
	@Override
	public void createMeeting(Meeting meeting) {
		meetingMapper.createMeeting(meeting);
	}                               
	
	@Override
	public List<MainPageResDto.MonthlySummary> getMonthlySummaries (Long userId, LocalDateTime start, LocalDateTime end) {
		return meetingMapper.selectMonthlySummaries(userId, start, end);
	}
	
	@Override
	public List<MainPageResDto.MeetingDetail> getMeetingDetailsByDate (Long userId, LocalDateTime start, LocalDateTime end) {
		return meetingMapper.selectMeetingDetailsByDate(userId, start, end);
	}
	
	@Override
	public StepResDto getStep(Long meetingId) {
		return meetingMapper.getStep(meetingId);
	}
	
	@Override
	public void updateMeetingStatus(Long meetingId, int meeting_status) {
		meetingMapper.updateMeetingStatus(meetingId, meeting_status);
	}
	
	@Override
	public List<TimetableAlgorithmQueryDto> getSortTimetable(Long meetingId) {
		return meetingMapper.getSortTimetable(meetingId);
	}
	
	@Override
	public void confirmMeeting(Long meetingId, LocalDateTime startTime, LocalDateTime endTime) {
		meetingMapper.confirmMeeting(meetingId, startTime, endTime);
	}
	
	@Override
	public void deleteMeeting(Long meetingId) {
		meetingMapper.deleteMeeting(meetingId);
	}
	
	@Override
	public MeetingDetailResDto.MeetingInfo getMeetingDetail(Long meetingId) {
		return meetingMapper.getMeetingDetail(meetingId);
	}
	
	@Override
	public void updateMeeting(Long meetingId, MeetingUpdateReqDto dto) {
		meetingMapper.updateMeeting(meetingId, dto);
	}
}
