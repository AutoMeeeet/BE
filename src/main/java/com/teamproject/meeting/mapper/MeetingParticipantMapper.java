package com.teamproject.meeting.mapper;

import org.apache.ibatis.annotations.Mapper;
<<<<<<< HEAD
=======
import org.apache.ibatis.annotations.Param;

import com.teamproject.meeting.enums.meetingparticipant.Permission;
import com.teamproject.meeting.enums.meetingparticipant.Role;
>>>>>>> origin/dev

@Mapper
public interface MeetingParticipantMapper {

<<<<<<< HEAD
=======
	void createMeeting(
		@Param("meetingId") Long meetingId,
		@Param("userId") Long userId,
		@Param("emailNotification") Boolean emailNotification,
		@Param("role") Role role,
		@Param("permission") Permission permission,
		@Param("timetableCase") Boolean timetableCase,
		@Param("voteCase") Boolean voteCase
	);
>>>>>>> origin/dev
}
