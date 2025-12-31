package com.teamproject.meeting.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.teamproject.meeting.dto.meeting.MeetingListReqDto;
import com.teamproject.meeting.entity.Meeting;
import com.teamproject.meeting.enums.meeting.MeetingState;

@Mapper
public interface MeetingMapper {

    List<MeetingListReqDto> getMeetings(
        @Param("userId") Long userId,
        @Param("state") MeetingState state
    );
    
    void createMeeting(Meeting meeting);
}
