package com.teamproject.meeting.dto.meetingParticipant;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PermissionUpdateReqDto {
	private String role;
	private String permission;
}
