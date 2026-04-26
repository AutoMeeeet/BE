package com.teamproject.meeting.dto.meetingParticipant;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingUpdateReqDto {
	private String title;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private String locationType;
	private String location;
	private Integer capacity;
}
