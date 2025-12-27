package com.teamproject.meeting.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MeetingReferenceMapper {

	void createMeeting(
		@Param("meetingId") Long meetingId,
		@Param("referenceUrl") String referenceUrl
	);
}
