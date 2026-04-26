package com.teamproject.meeting.dto.meetingParticipant;

import java.time.LocalDateTime;
import java.util.List;

import com.teamproject.meeting.enums.meeting.LocationType;
import com.teamproject.meeting.enums.meeting.MeetingState;
import com.teamproject.meeting.enums.meetingparticipant.Permission;
import com.teamproject.meeting.enums.meetingparticipant.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MeetingDetailResDto {
	
	private String title;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private MeetingState meetingState;
	private LocationType locationType;
	private String location;
	private List<Reference> references;
	private List<Minutes> minutes;
	private List<Participant> participants;
	
	@Getter
	@AllArgsConstructor
	public static class Reference {
		private Long referenceId;
		private String referenceUrl;
	}
	
	@Getter
	@AllArgsConstructor
	public static class Minutes {
		private Long minutesId;
		private String minutesUrl;
	}
	
	@Getter
	@AllArgsConstructor
	public static class Participant {
		private Long participantId;
		private boolean isMe;
		private String nickname;
		private Permission permission;
		private Role role;// 방장 여부
	}
	
	// Service에서 해당 내용을 받아오기 위한 DTO
	@Getter
	@AllArgsConstructor
	public static class MeetingInfo {
	    private String title;
	    private LocalDateTime startTime;
	    private LocalDateTime endTime;
	    private MeetingState meetingState;
	    private LocationType locationType;
	    private String location;
	}
}
