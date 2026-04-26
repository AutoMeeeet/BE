package com.teamproject.meeting.mapper;

import org.apache.ibatis.annotations.Mapper;
<<<<<<< HEAD
=======
import org.apache.ibatis.annotations.Param;
>>>>>>> origin/dev

@Mapper
public interface MeetingReferenceMapper {

<<<<<<< HEAD
=======
	void createMeeting(
		@Param("meetingId") Long meetingId,
		@Param("referenceUrl") String referenceUrl
	);
>>>>>>> origin/dev
}
