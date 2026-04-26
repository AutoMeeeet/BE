package com.teamproject.meeting.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.teamproject.meeting.dto.meetingParticipant.MeetingDetailResDto;

@Mapper
public interface MinutesMapper {
	
	void createMeeting(
		@Param("meetingId") Long meetingId,
		@Param("minutesUrl") String minutesUrl
	);
	
	List<MeetingDetailResDto.Minutes> findMeetingId(@Param("meetingId") Long meetingId);
	
	Long findMeetingIdByMinutesId(@Param("minutesId") Long minutesId);
	
	void deleteById(@Param("minutesId") Long minutesId);
}
