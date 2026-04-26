package com.teamproject.meeting.dto.meetingParticipant;

import java.time.LocalDateTime;

import com.teamproject.meeting.enums.meetingparticipant.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TimetableQueryDto {
	private Long userId;
	private String nickname;
	private Role role;
	private boolean timetableCase;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
}
