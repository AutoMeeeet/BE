package com.teamproject.meeting.controller.meeting;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.teamproject.meeting.dto.meeting.MeetingListDto;
import com.teamproject.meeting.enums.meeting.MeetingState;
import com.teamproject.meeting.enums.meeting.MeetingTimeType;
import com.teamproject.meeting.infrastructure.security.local.CustomUserDetails;
import com.teamproject.meeting.service.meeting.MeetingListService;

@RestController
public class MeetingListController {
	
	private final MeetingListService meetingListService;
	
	public MeetingListController(MeetingListService meetingListService) {
		this.meetingListService = meetingListService;
	}
	
	@GetMapping("/meetinglist")
	public List<MeetingListDto> getMeetings(
			@AuthenticationPrincipal CustomUserDetails user,
			@RequestParam(required = false) MeetingState state,
			@RequestParam(required = false) MeetingTimeType timeType
	) {
		
		return meetingListService.getMeetings(user.getUserId(), state, timeType);
	}
}