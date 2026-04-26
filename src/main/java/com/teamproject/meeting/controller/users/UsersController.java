package com.teamproject.meeting.controller.users;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamproject.meeting.dto.users.JoinReqDto;
import com.teamproject.meeting.dto.users.MyPageResDto;
import com.teamproject.meeting.dto.users.NickNameReqDto;
import com.teamproject.meeting.dto.users.PwReqDto;
import com.teamproject.meeting.infrastructure.common.CommonResponse;
import com.teamproject.meeting.infrastructure.security.local.CustomUserDetails;
import com.teamproject.meeting.service.users.UsersService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;

@RestController
@RequestMapping("/users")
public class UsersController {

	private final UsersService usersService;
	
	public UsersController(UsersService usersService) {
		this.usersService = usersService;
	}
	
	@Operation(summary = "회원가입")
	@SecurityRequirements
	@PostMapping
	public ResponseEntity<CommonResponse> joinProcess(@Valid @RequestBody JoinReqDto joinDto) {
		usersService.joinProcess(joinDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResponse("회원가입 성공"));
	}
	
	@Operation(summary = "비밀번호 변경")
	@PutMapping("/me/password")
	public ResponseEntity<CommonResponse> changePW(@AuthenticationPrincipal CustomUserDetails principal, @Valid @RequestBody PwReqDto pw) {
		usersService.changePW(principal.getUserId(), pw);
		return ResponseEntity.status(HttpStatus.OK).body(new CommonResponse("PW 변경 성공"));
	}
	
	@Operation(summary = "닉네임 변경")
	@PutMapping("/me/nickname")
	public ResponseEntity<CommonResponse> changeNickname(@AuthenticationPrincipal CustomUserDetails principal, @Valid @RequestBody NickNameReqDto name) {
		usersService.changeNickname(principal.getUserId(), name);
		return ResponseEntity.status(HttpStatus.OK).body(new CommonResponse("이름 변경 성공"));
	}
	
	@Operation(summary = "마이페이지")
	@GetMapping("/me")
	public ResponseEntity<MyPageResDto> getMyPage(@AuthenticationPrincipal CustomUserDetails user) {
		MyPageResDto myPage = new MyPageResDto(
			user.getNickname(),
			user.getUsername(), // email
			user.getProvider()
		);
		return ResponseEntity.ok(myPage);
	}
}