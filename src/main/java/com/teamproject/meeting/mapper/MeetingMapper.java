package com.teamproject.meeting.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.teamproject.meeting.domain.timetableAlgorithm.TimetableAlgorithmQueryDto;
import com.teamproject.meeting.dto.meeting.MainPageResDto;
import com.teamproject.meeting.dto.meeting.MeetingListReqDBDto;
import com.teamproject.meeting.dto.meetingParticipant.MeetingDetailResDto;
import com.teamproject.meeting.dto.meetingParticipant.MeetingUpdateReqDto;
import com.teamproject.meeting.dto.meetingParticipant.StepResDto;
import com.teamproject.meeting.entity.Meeting;
import com.teamproject.meeting.enums.meeting.MeetingState;

@Mapper
public interface MeetingMapper {

    List<MeetingListReqDBDto> getMeetings(
        @Param("userId") Long userId,
        @Param("state") MeetingState state
    );
    
    void createMeeting(Meeting meeting);
    
    List<MainPageResDto.MonthlySummary> selectMonthlySummaries(
    	@Param("userId") Long userId,
    	@Param("start") LocalDateTime start,
    	@Param("end") LocalDateTime end
    );
    
    List<MainPageResDto.MeetingDetail> selectMeetingDetailsByDate(
    	@Param("userId") Long userId,
        @Param("start") LocalDateTime start,
       	@Param("end") LocalDateTime end
    );
    
    StepResDto getStep(Long meetingId);
    
    void updateMeetingStatus(@Param("meetingId") Long meetingId, @Param("meeting_status") int meeting_status);
    
    List<TimetableAlgorithmQueryDto> getSortTimetable(@Param("meetingId") Long meetingId);

    void confirmMeeting(
    	@Param("meetingId") Long meetingId,
    	@Param("startTime") LocalDateTime startTime,
    	@Param("endTime") LocalDateTime endTime
    );
    
    void deleteMeeting(@Param("meetingId") Long meetingId);
    
    MeetingDetailResDto.MeetingInfo getMeetingDetail(@Param("meetingId") Long meetingId);
    
    void updateMeeting(@Param("meetingId") Long meetingId, @Param("dto") MeetingUpdateReqDto dto);
}
