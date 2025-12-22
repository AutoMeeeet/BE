package com.teamproject.meeting.controller.users;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.teamproject.meeting.dto.users.NickNameDto;
import com.teamproject.meeting.infrastructure.common.CommonResponse;
import com.teamproject.meeting.infrastructure.security.local.CustomUserDetails;
import com.teamproject.meeting.service.users.NickNameService;

import jakarta.validation.Valid;

@RestController
public class NickNameController {
	
	private final NickNameService nickNameService;
	
	public NickNameController(NickNameService nickNameService) {
		this.nickNameService = nickNameService;
	}
	
	@PutMapping("/nickname")
	public ResponseEntity<CommonResponse> changeNickname(@AuthenticationPrincipal CustomUserDetails principal, @Valid @RequestBody NickNameDto dto) {
		nickNameService.changeNickname(dto.getNickname(), principal.getUserId());
		return ResponseEntity.status(HttpStatus.OK).body(new CommonResponse("이름 변경 성공"));
	}
}