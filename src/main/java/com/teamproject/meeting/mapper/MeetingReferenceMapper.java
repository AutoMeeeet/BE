package com.teamproject.meeting.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.teamproject.meeting.dto.meetingParticipant.MeetingDetailResDto;

@Mapper
public interface MeetingReferenceMapper {

	void createMeeting(
		@Param("meetingId") Long meetingId,
		@Param("referenceUrl") String referenceUrl
	);
	
	List<MeetingDetailResDto.Reference> findByMeetingId(@Param("meetingId") Long meetingId);
	
	Long findMeetingIdByReferenceId(@Param("referenceId") Long referenceId);

	void deleteById(@Param("referenceId") Long referenceId);
}
