package com.teamproject.meeting.controller.meetingParticipant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teamproject.meeting.dto.meetingParticipant.ConfirmVoteReqDto;
import com.teamproject.meeting.dto.meetingParticipant.CreateAvailabilityReqDto;
import com.teamproject.meeting.dto.meetingParticipant.MeetingDetailResDto;
import com.teamproject.meeting.dto.meetingParticipant.MeetingUpdateReqDto;
import com.teamproject.meeting.dto.meetingParticipant.PermissionUpdateReqDto;
import com.teamproject.meeting.dto.meetingParticipant.StepResDto;
import com.teamproject.meeting.dto.meetingParticipant.TimetableResDto;
import com.teamproject.meeting.dto.meetingParticipant.VoteReqDto;
import com.teamproject.meeting.dto.meetingParticipant.VoteResDto;
import com.teamproject.meeting.dto.meetingParticipant.VoteResultResDto;
import com.teamproject.meeting.infrastructure.security.local.CustomUserDetails;
import com.teamproject.meeting.service.meeting.MeetingService;
import com.teamproject.meeting.service.meetingparticipant.MeetingParticipantService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/meeting-participant")
public class MeetingParticipantController {
	
	private final MeetingService meetingService;
	private final MeetingParticipantService meetingParticipantService;
	
	public MeetingParticipantController(MeetingService meetingService, MeetingParticipantService meetingParticipantService) {
		this.meetingService = meetingService;
		this.meetingParticipantService = meetingParticipantService;
	}
	
	// 시간표 조회(확정을 한 팀원들 시간표까지) -> 시간표 투표 -> 회의 상세 보기
	// - 페이지 조회를 조회
	@Operation(summary = "회의 Step 조회", description = "1인 경우 시간표, 2인 경우 투표, 3인 경우 투표 확정 페이지, 4인 경우 회의 상세페이지")
	@GetMapping("/{meetingId}/step")
	public ResponseEntity<StepResDto> getStep(
		@PathVariable Long meetingId) {
	
		return ResponseEntity.ok(meetingService.getStep(meetingId));
	};
	
	// - 회의 상세 페이지로 들어가는 경우 시간표 확정을 안하고 && 투표를 하지 않은 경우 시간표 조회 페이지로 이동
	@Operation(summary = "회의 상세 페이지_시간표 조회")
	@GetMapping("/{meetingId}/timetable")
	public ResponseEntity<TimetableResDto> getTimeTable(
		@PathVariable Long meetingId, @AuthenticationPrincipal CustomUserDetails user) {
		
		Long userId = user.getUserId();
		return ResponseEntity.ok(meetingParticipantService.getTimetable(meetingId, userId));
	}; 
	
	// 시간표 확정
	@Operation(summary = "시간표 확정")
	@PostMapping("/{meetingId}/timetable")
	public ResponseEntity<Void> confirmTimetable(
		@PathVariable Long meetingId,
		@AuthenticationPrincipal CustomUserDetails user) {
	
		Long userId = user.getUserId();
		meetingParticipantService.confirmTimetable(meetingId, userId);
		return ResponseEntity.ok().build();
	}
	
	// - 회의 상세 페이지로 들어가는 경우 시간표 확정을 하고 && 투표를 하지 않은 경우라면 시간표 투표 페이지로 이동
	@Operation(summary = "회의 상세 페이지_투표 페이지")
	@GetMapping("/{meetingId}/vote-page")
	public ResponseEntity<VoteResDto> getVotePage(
		@PathVariable Long meetingId,
		@AuthenticationPrincipal CustomUserDetails user) {
		
		Long userId = user.getUserId();
		return ResponseEntity.ok(meetingParticipantService.getVotePage(meetingId, userId));
	}
	
	// 투표
	@Operation(summary = "투표")
	@PostMapping("/{meetingId}/vote")
	public ResponseEntity<Void> vote(
		@PathVariable Long meetingId,
		@RequestBody VoteReqDto dto,
		@AuthenticationPrincipal CustomUserDetails user) {
		
		Long userId = user.getUserId();
		meetingParticipantService.vote(userId, meetingId, dto.getCandidateId());
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	// 투표 결과 조회
	@Operation(summary = "투표 결과 조회")
	@GetMapping("/{meetingId}/vote/result")
	public ResponseEntity<VoteResultResDto> getVoteResult(
		@PathVariable Long meetingId,
		@AuthenticationPrincipal CustomUserDetails user) {
		
		Long userId = user.getUserId();
		return ResponseEntity.ok(meetingParticipantService.getVoteResult(meetingId, userId));
	}
	
	// 투표 결과(방장) 확정
	@Operation(summary = "투표 결과 확정")
	@PostMapping("/{meetingId}/confirm")
	public ResponseEntity<Void> confirmVote(
		@PathVariable Long meetingId,
		@RequestBody ConfirmVoteReqDto dto,
		@AuthenticationPrincipal CustomUserDetails user) {
		
		Long userId = user.getUserId();
		meetingParticipantService.confirmVote(meetingId, userId, dto);
		return ResponseEntity.ok().build();
	}
	
	// - 회의 상세 페이지로 들어가는 경우 시간표 확정을 하고 && 투표를 한 경우 회의 상세 페이지로 이동
	@Operation(summary = "회의 상세 페이지_회의 상세 페이지")
	@GetMapping("/{meetingId}/meeting-detail-page")
	public ResponseEntity<MeetingDetailResDto> getMeetingDetailPage(
		@PathVariable Long meetingId,
		@AuthenticationPrincipal CustomUserDetails user) {
		
		Long userId = user.getUserId();
		return ResponseEntity.ok(meetingService.getMeetingDetailPage(meetingId, userId));
	}
	
	// 시간표 생성
	@Operation(summary = "시간표 생성")
	@PostMapping("/{meetingId}/availability")
	public ResponseEntity<Void> createAvailability(
		@PathVariable Long meetingId,
		@AuthenticationPrincipal CustomUserDetails user,
		@RequestBody CreateAvailabilityReqDto dto) {
		
		Long userId = user.getUserId();
		meetingParticipantService.createAvailability(meetingId, userId, dto.getStartTime(), dto.getEndTime());
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
	
	// 시간표 수정(보류)
	
	// 시간표 삭제
	@Operation(summary = "시간표 삭제")
	@DeleteMapping("/{meetingId}/{availabilityId}")
	public ResponseEntity<Void> deleteAvailability(
		@PathVariable Long meetingId,
		@PathVariable Long availabilityId,
		@AuthenticationPrincipal CustomUserDetails user) {
		
		Long userId = user.getUserId();
		meetingParticipantService.deleteAvailability(meetingId, userId, availabilityId);
		return ResponseEntity.noContent().build();
	}
	
	// 회의 수정
	// - Team 내 권한이 수정 가능 이상인 경우 회의를 수정할 수 있다.
	@Operation(summary = "회의 수정")
	@PatchMapping("/{meetingId}")
	public ResponseEntity<Void> updateMeeting(
		@PathVariable Long meetingId,
		@AuthenticationPrincipal CustomUserDetails user,
		@RequestBody MeetingUpdateReqDto dto) {
		
		meetingService.updateMeeting(meetingId, user.getUserId(), dto);
		return ResponseEntity.ok().build();
	}
	
	// Team 권한 수정
	// - Team 내 권한 부여 권한인 경우 팀원들의 권한을 줄 수 있다.
	@Operation(summary = "권한 수정")
	@PatchMapping("/{meetingId}/{targetUserId}/permission")
	public ResponseEntity<Void> updatePermission(
		@PathVariable Long meetingId,
		@PathVariable Long targetUserId,
		@AuthenticationPrincipal CustomUserDetails user,
		@RequestBody PermissionUpdateReqDto dto) {
		
		meetingParticipantService.updatePermission(meetingId, user.getUserId(), targetUserId, dto);
		return ResponseEntity.ok().build();
	}
	
	// 회의 탈퇴
	// - 내용물은 그대로 보존하되 user만 탈퇴
	@Operation(summary = "회원 탈퇴")
	@DeleteMapping("/{meetingId}/leave")
	public ResponseEntity<Void> leaveMeeting(
		@PathVariable Long meetingId,
		@AuthenticationPrincipal CustomUserDetails user) {
		
		meetingParticipantService.leaveMeeting(meetingId, user.getUserId());
		return ResponseEntity.noContent().build();
	}
	
}