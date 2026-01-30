package com.teamproject.meeting.dto.meeting;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MeetingListResDto {
	
	private LocalDate date;
	
	private List<MeetingListReqDBDto> meetings;
}
