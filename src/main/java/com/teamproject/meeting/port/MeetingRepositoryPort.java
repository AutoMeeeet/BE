package com.teamproject.meeting.port;

import java.time.LocalDateTime;
import java.util.List;

import com.teamproject.meeting.domain.timetableAlgorithm.TimetableAlgorithmQueryDto;
import com.teamproject.meeting.dto.meeting.MainPageResDto;
import com.teamproject.meeting.dto.meeting.MeetingListReqDBDto;
import com.teamproject.meeting.dto.meetingParticipant.MeetingDetailResDto;
import com.teamproject.meeting.dto.meetingParticipant.MeetingUpdateReqDto;
import com.teamproject.meeting.dto.meetingParticipant.StepResDto;
import com.teamproject.meeting.entity.Meeting;
import com.teamproject.meeting.enums.meeting.MeetingState;

public interface MeetingRepositoryPort {
	
	List<MeetingListReqDBDto> getMeetings(Long userId, MeetingState state);
	
	void createMeeting(Meeting meeting);
	
	List<MainPageResDto.MonthlySummary> getMonthlySummaries(Long userId, LocalDateTime start, LocalDateTime end);
	
	List<MainPageResDto.MeetingDetail> getMeetingDetailsByDate(Long userId, LocalDateTime start, LocalDateTime end);
	
	StepResDto getStep(Long meetingId);
	
	void updateMeetingStatus(Long meetingId, int meeting_status);
	
	List<TimetableAlgorithmQueryDto> getSortTimetable(Long meetingId);
	
	void confirmMeeting(Long meetingId, LocalDateTime startTime, LocalDateTime endTime);

	void deleteMeeting(Long meetingId);
	
	MeetingDetailResDto.MeetingInfo getMeetingDetail(Long meetingId);
	
	void updateMeeting(Long meetingId, MeetingUpdateReqDto dto);
}