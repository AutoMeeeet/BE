package com.teamproject.meeting.dto.meeting;

import java.time.LocalDateTime;

import com.teamproject.meeting.enums.meeting.LocationType;
import com.teamproject.meeting.enums.meeting.MeetingState;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeetingListReqDto {
	private String title;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private LocationType locationType;
	private MeetingState meetingState;
	private Integer capacity;
	private Integer participantCount;
	private String location;
}
