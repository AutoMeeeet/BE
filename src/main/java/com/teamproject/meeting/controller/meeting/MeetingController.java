package com.teamproject.meeting.controller.meeting;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.teamproject.meeting.dto.meeting.MeetingListResDto;

import com.teamproject.meeting.dto.meeting.AllReqDto;
import com.teamproject.meeting.dto.meeting.CreateMeetingReqDto;
import com.teamproject.meeting.dto.meeting.CreateMinutesReqDto;
import com.teamproject.meeting.dto.meeting.CreateReferenceReqDto;
import com.teamproject.meeting.dto.meeting.DailyReqDto;
import com.teamproject.meeting.dto.meeting.MainPageResDto;
import com.teamproject.meeting.dto.meeting.MonthReqDto;
import com.teamproject.meeting.infrastructure.common.UrlResponse;
import com.teamproject.meeting.infrastructure.security.local.CustomUserDetails;
import com.teamproject.meeting.service.meeting.MeetingService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/meetings")
public class MeetingController {

	private final MeetingService meetingService;

	public MeetingController(MeetingService meetingService) {
		this.meetingService = meetingService;

    }
	
	// Meeting 생성
	@Operation(summary = "회의 생성")
	@PostMapping
	public ResponseEntity<Void> createMeeting(
		@AuthenticationPrincipal CustomUserDetails user,
		@Valid @RequestBody CreateMeetingReqDto dto) {
		
		meetingService.createMeeting(user.getUserId(), dto);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	// MainPage
	@Operation(summary = "달 별로 일일 미팅 카운팅")
	@GetMapping("/month")
	public ResponseEntity<MainPageResDto.Main> getMonthMeetings(
		@AuthenticationPrincipal CustomUserDetails user, @Valid MonthReqDto date) {
		
		return ResponseEntity.ok(meetingService.getMonthMeetings(user.getUserId(), date));
	}
	
	// 일간 상세 조회
	@Operation(summary = "해당 날짜 클릭 시 미팅 확인")
	@GetMapping("/daily")
	public ResponseEntity<List<MainPageResDto.MeetingDetail>> getDailyMeetings(
		@AuthenticationPrincipal CustomUserDetails user, @Valid DailyReqDto date) {
		
		return ResponseEntity.ok(meetingService.getDailyMeetings(user.getUserId(), date));
	}
	
	@Operation(summary = "전체 회의 조회")
	@GetMapping
	public ResponseEntity<List<MeetingListResDto>> getMeetings(
		@AuthenticationPrincipal CustomUserDetails user, @Valid AllReqDto all) {
		
		return ResponseEntity.ok(meetingService.getMeetings(user.getUserId(), all));
	}

    // [추가] 초대 코드로 입장 API
	@Operation(summary = "초대 코드로 입장")
    @PostMapping("/{token}/join")
    public ResponseEntity<UrlResponse> joinMeeting(
    		@AuthenticationPrincipal CustomUserDetails user,
            @PathVariable("token") String token) {
        Long meetingId = meetingService.joinMeeting(user.getUserId(), token);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UrlResponse("초대코드 생성 ", "/meetings/" + meetingId));
    }
    
	@Operation(summary = "회의 삭제")
    @DeleteMapping("/{meetingId}/delete")
	public ResponseEntity<Void> deleteMeeting(
		@PathVariable Long meetingId) {
		
		meetingService.deleteMeeting(meetingId);
		return ResponseEntity.noContent().build(); // 204 No Content 반환
	}
	
	@Operation(summary = "참고 자료 생성")
	@PostMapping("/{meetingId}/createReference")
	public ResponseEntity<Void> createReference(
		@PathVariable Long meetingId,
		@AuthenticationPrincipal CustomUserDetails user,
		@RequestBody CreateReferenceReqDto dto) {
		
		meetingService.createReference(user.getUserId(), meetingId, dto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@Operation(summary = "참고 자료 삭제")
	@DeleteMapping("/{referenceId}/deleteReference")
	public ResponseEntity<Void> deleteReference(
		@PathVariable Long referenceId,
		@AuthenticationPrincipal CustomUserDetails user) {
		
		meetingService.deleteReference(user.getUserId(), referenceId);
		return ResponseEntity.noContent().build();
	}
	
	@Operation(summary = "회의록 자료 생성")
	@PostMapping("/{meetingId}/createMinutes")
	public ResponseEntity<Void> createMinutes(
		@PathVariable Long meetingId,
		@AuthenticationPrincipal CustomUserDetails user,
		@RequestBody CreateMinutesReqDto dto) {
		
		meetingService.createMinutes(user.getUserId(), meetingId, dto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	@Operation(summary = "회의록 자료 삭제")
	@DeleteMapping("/{minutesId}/deleteMinutes")
	public ResponseEntity<Void> deleteMinutes(
		@PathVariable Long minutesId,
		@AuthenticationPrincipal CustomUserDetails user) {
		
		meetingService.deleteMinutes(user.getUserId(), minutesId);
		return ResponseEntity.noContent().build();
	}
}