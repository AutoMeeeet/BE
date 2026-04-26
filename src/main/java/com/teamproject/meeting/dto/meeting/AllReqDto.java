package com.teamproject.meeting.dto.meeting;

import com.teamproject.meeting.enums.meeting.MeetingState;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder // Test용
public class AllReqDto {
	
	@NotNull
	private MeetingState meetingState;
}