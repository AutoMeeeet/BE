package com.teamproject.meeting.dto.meeting;

import java.time.LocalDateTime;

import com.teamproject.meeting.enums.meeting.LocationType;
import com.teamproject.meeting.enums.meeting.MeetingState;

import lombok.Getter;

@Getter
public class MeetingListDto {
	private String title;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private LocationType locationType;
	private MeetingState meetingState;
	private Integer capacity;
	private Integer participantCount;
	private String location;
}
