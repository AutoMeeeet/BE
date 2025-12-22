package com.teamproject.meeting.controller.users;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.teamproject.meeting.dto.users.PWDto;
import com.teamproject.meeting.infrastructure.common.CommonResponse;
import com.teamproject.meeting.infrastructure.security.local.CustomUserDetails;
import com.teamproject.meeting.service.users.PWService;

import jakarta.validation.Valid;

@RestController
public class PWController {
	
	private final PWService pwService;
	
	public PWController(PWService pwService) {
		this.pwService = pwService;
	}

	@PutMapping("/password")
	public ResponseEntity<CommonResponse> changePW(@AuthenticationPrincipal CustomUserDetails principal, @Valid @RequestBody PWDto dto) {
		pwService.changePW(principal.getUserId(), dto.getCurrentPW(), dto.getNewPW());
		return ResponseEntity.status(HttpStatus.OK).body(new CommonResponse("PW 변경 성공"));
	}
}