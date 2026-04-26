package com.teamproject.meeting.dto.meeting;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.teamproject.meeting.enums.meeting.MeetingState;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class MainPageResDto {
	
	@Getter @Builder @AllArgsConstructor
	public static class Main {
		
		private List<MonthlySummary> monthlySummaries;
		
		private List<MeetingDetail> todayMeetings;
	}
	
	@Getter @Builder @AllArgsConstructor
	public static class MonthlySummary {  // 월간 요약
		
		private LocalDate date;  // 달력용
		
		private Integer count;
	}
	
	@Getter @Builder @AllArgsConstructor
	public static class MeetingDetail {  // 날짜별 회의
		
		private Long meetingId; // 상세 페이지 이동
		
		private String title;
		
		private LocalDateTime startTime;
		
		private LocalDateTime endTime;
		
		private String location; // 주소 or 비대면
		
		private Integer participantCount;
		
		private Integer capacity; // 정원
		
		@Schema(description = "확정/미확정/종료", allowableValues = {"CONFIRMED", "PENDING", "FINISHED"})
		private MeetingState meetingState; // 확정, 미확정, 종료
	}
}