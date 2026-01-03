package com.teamproject.meeting.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MinutesMapper {
	
	void createMeeting(
		@Param("meetingId") Long meetingId,
		@Param("minutesUrl") String minutesUrl
	);

}
