package com.teamproject.meeting.dto.meeting;

import java.time.LocalDateTime;

import com.teamproject.meeting.enums.meeting.LocationType;
import com.teamproject.meeting.enums.meeting.MeetingState;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MeetingListReqDBDto {

	private String title;

	private LocalDateTime startTime;

	private LocalDateTime endTime;

	private LocationType locationType;

	@Schema(description = "확정/미확정/종료", allowableValues = {"CONFIRMED", "PENDING", "FINISHED"})
	private MeetingState meetingState;

	private Integer capacity;

	private Integer participantCount;

	private String location;
}
