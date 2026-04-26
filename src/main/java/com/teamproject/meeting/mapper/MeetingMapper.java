package com.teamproject.meeting.mapper;

<<<<<<< HEAD
import org.apache.ibatis.annotations.Mapper;
=======
import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.teamproject.meeting.dto.meeting.MainPageResDto;
import com.teamproject.meeting.dto.meeting.MeetingListReqDBDto;
import com.teamproject.meeting.entity.Meeting;
import com.teamproject.meeting.enums.meeting.MeetingState;
>>>>>>> origin/dev

@Mapper
public interface MeetingMapper {

<<<<<<< HEAD
=======
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
>>>>>>> origin/dev
}
