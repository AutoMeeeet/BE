package com.teamproject.meeting.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.teamproject.meeting.dto.meetingParticipant.MeetingDetailResDto;
import com.teamproject.meeting.dto.meetingParticipant.PermissionUpdateReqDto;
import com.teamproject.meeting.dto.meetingParticipant.TimetableQueryDto;
import com.teamproject.meeting.dto.meetingParticipant.VoteResDto;
import com.teamproject.meeting.entity.MeetingAvailability;
import com.teamproject.meeting.enums.meetingparticipant.Permission;
import com.teamproject.meeting.enums.meetingparticipant.Role;

@Mapper
public interface MeetingParticipantMapper {

	void createMeeting(
		@Param("meetingId") Long meetingId,
		@Param("userId") Long userId,
		@Param("emailNotification") Boolean emailNotification,
		@Param("role") Role role,
		@Param("permission") Permission permission,
		@Param("timetableCase") Boolean timetableCase,
		@Param("voteCase") Boolean voteCase
	);
	
	List<TimetableQueryDto> getTimetable(@Param("meetingId") Long meetingId);
	
	Long findParticipantId(@Param("meetingId") Long meetingId, @Param("userId") Long userId);

	int confirmTimetable(@Param("participantId") Long participantId);
	
	int countUnconfirmedTimetable(@Param("meetingId") Long meetingId);
	
	List<VoteResDto.VoteCandidate> getVoteCandidates(@Param("meetingId") Long meetingId, @Param("userId") Long userId);
	
	void createVoteRecord(@Param("userId") Long userId, @Param("meetingId") Long meetingId, @Param("candidateId") Long candidateId);
	
	int countUnvotedParticipant(@Param("meetingId") Long meetingId);

	boolean isOrganizer(@Param("meetingId") Long meetingId, @Param("userId") Long userId);

	boolean existByMeetingIdAndUserId(@Param("meetingId") Long meetingId, @Param("userId") Long userId);
	
	List<MeetingDetailResDto.Participant> findParticipants(@Param("meetingId") Long meetingId, @Param("userId") Long userId);
	
	void createAvailability(MeetingAvailability availability);
	
	void deleteAvailability(@Param("participantId") Long participantId, @Param("availabilityId") Long availabilityId);

	String findPermission(@Param("meetingId") Long meetingId, @Param("userId") Long userId);
	
	void updatePermission(@Param("meetingId") Long meetingId, @Param("targetUserId") Long targetUserId, @Param("dto") PermissionUpdateReqDto dto);
	
	void deleteParticipant(@Param("participantId") Long participantId);
}