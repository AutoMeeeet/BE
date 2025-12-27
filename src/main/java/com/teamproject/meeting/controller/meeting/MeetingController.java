package com.teamproject.meeting.controller.meeting;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamproject.meeting.dto.meeting.CreateMeetingDto;
import com.teamproject.meeting.infrastructure.common.UrlResponse;
import com.teamproject.meeting.infrastructure.security.local.CustomUserDetails;
import com.teamproject.meeting.service.meeting.MeetingService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
public class MeetingController {

	private final MeetingService meetingService;
	
	public MeetingController(MeetingService meetingService) {
		this.meetingService = meetingService;
	}
	
	// Meeting 생성
	@PostMapping("/createmeeting")
	public ResponseEntity<UrlResponse> CreateMeeting(
			@AuthenticationPrincipal CustomUserDetails user,
			@Valid @RequestBody CreateMeetingDto dto) {
		
		String token = meetingService.createMeeting(user.getUserId(), dto);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(new UrlResponse("Meeting 생성 성공", "/meetings/" + token));
	}
}