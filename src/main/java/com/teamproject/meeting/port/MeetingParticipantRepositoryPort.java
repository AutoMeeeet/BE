package com.teamproject.meeting.port;

import java.util.List;

import com.teamproject.meeting.dto.meetingParticipant.MeetingDetailResDto;
import com.teamproject.meeting.dto.meetingParticipant.PermissionUpdateReqDto;
import com.teamproject.meeting.dto.meetingParticipant.TimetableQueryDto;
import com.teamproject.meeting.dto.meetingParticipant.VoteResDto;
import com.teamproject.meeting.entity.MeetingAvailability;
import com.teamproject.meeting.enums.meetingparticipant.Permission;
import com.teamproject.meeting.enums.meetingparticipant.Role;

public interface MeetingParticipantRepositoryPort {

	void createMeeting(
		Long meetingId, 
		Long userId, 
		Boolean emailNotification,
		Role role,
		Permission permission,
		Boolean timetableCase,
		Boolean voteCase
	);
	
	List<TimetableQueryDto> getTimetable(Long meetingId);
	
	Long findParticipantId(Long meetingId, Long userId);
	
	int confirmTimetable(Long participantId);
	
	int countUnconfirmedTimetable(Long meetingId);
	
	List<VoteResDto.VoteCandidate> getVoteCandidates(Long meetingId, Long userId);

	void createVoteRecord(Long userId, Long meetingId, Long candidateId);
	
	int countUnvotedParticipant(Long meetingId);
	
	boolean isOrganizer(Long meetingId, Long userId);
	
	boolean existByMeetingIdAndUserId(Long meetingId, Long userId);
	
	List<MeetingDetailResDto.Participant> findParticipants(Long meetingId, Long userId);
	
	void createAvailability(MeetingAvailability availability);
	
	void deleteAvailability(Long participantId, Long availabilityId);
	
	String findPermission(Long meetingId, Long userId);
	
	void updatePermission(Long meetingId, Long targetUserId, PermissionUpdateReqDto dto);
	
	void deleteParticipant(Long participantId);
}