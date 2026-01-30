package com.teamproject.meeting.dto.meeting;

import java.time.LocalDateTime;

import com.teamproject.meeting.enums.meeting.MeetingState;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class DailyResDto {
	
	private String title;
	
	private LocalDateTime startTime;
	
	private LocalDateTime endTime;
	
	private String location; // 주소 or 비대면
	
	private Integer headCount;
	
	private Integer capacity; // 정원
	
	@Schema(description = "확정/미확정/종료", allowableValues = {"CONFIRMED", "PENDING", "FINISHED"})
	private MeetingState meetingState; // 확정, 미확정, 종료
}
