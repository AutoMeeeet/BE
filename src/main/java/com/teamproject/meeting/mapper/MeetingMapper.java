package com.teamproject.meeting.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.teamproject.meeting.dto.meeting.MeetingListDto;
import com.teamproject.meeting.enums.meeting.MeetingState;
import com.teamproject.meeting.enums.meeting.MeetingTimeType;

@Mapper
public interface MeetingMapper {

    List<MeetingListDto> getMeetings(
        @Param("userId") Long userId,
        @Param("state") MeetingState state,
        @Param("timeType") MeetingTimeType timeType
    );
}
