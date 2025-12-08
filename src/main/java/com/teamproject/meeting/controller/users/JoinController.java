package com.teamproject.meeting.controller.users;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.teamproject.meeting.dto.users.JoinDto;
import com.teamproject.meeting.infrastructure.common.CommonResponse;
import com.teamproject.meeting.service.users.JoinService;

@RestController
public class JoinController {

	private final JoinService joinService;
	
	public JoinController(JoinService joinService) {
		this.joinService = joinService;
	}
	
	@PostMapping("/join")
	public ResponseEntity<CommonResponse> joinProcess(@RequestBody JoinDto joinDto) {
		
		joinService.joinProcess(joinDto);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResponse("회원가입 성공"));
	}
}