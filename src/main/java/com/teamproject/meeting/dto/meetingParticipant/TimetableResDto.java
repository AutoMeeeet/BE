package com.teamproject.meeting.dto.meetingParticipant;

import java.time.LocalDateTime;
import java.util.List;

import com.teamproject.meeting.enums.meetingparticipant.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TimetableResDto {

	private List<ParticipantTimetable> timetables;
	
	@Getter
	@Builder
	@AllArgsConstructor
	public static class ParticipantTimetable {
		private String nickname;
		private Role role;
		private boolean isMe; // 본인 여부
		private boolean timetableCase; // 시간표 확정 여부
		private List<TimeSlot> timeSlots;
	}
	
	@Getter
	@Builder
	@AllArgsConstructor
	public static class TimeSlot {
		private LocalDateTime startTime;
		private LocalDateTime endTime;
	}
}