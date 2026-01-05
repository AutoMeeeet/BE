package com.teamproject.meeting.controller.meeting;

import com.teamproject.meeting.dto.meeting.MeetingListResDto;
import com.teamproject.meeting.enums.meeting.MeetingState;
import com.teamproject.meeting.service.meeting.MeetingListService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.teamproject.meeting.dto.meeting.CreateMeetingDto;
import com.teamproject.meeting.infrastructure.common.UrlResponse;
import com.teamproject.meeting.infrastructure.security.local.CustomUserDetails;
import com.teamproject.meeting.service.meeting.MeetingService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

import java.util.List;

@RestController
public class MeetingController {

	private final MeetingService meetingService;
    private final MeetingListService meetingListService;
	public MeetingController(MeetingService meetingService, MeetingListService meetingListService) {
		this.meetingService = meetingService;
        this.meetingListService = meetingListService;
    }
	
	// Meeting 생성
	@PostMapping("/createmeeting")
	public ResponseEntity<UrlResponse> CreateMeeting(
			@AuthenticationPrincipal CustomUserDetails user,
			@Valid @RequestBody CreateMeetingDto dto) {
		
		String token = meetingService.createMeeting(user.getUserId(), dto);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(new UrlResponse("Meeting 생성 성공", "/meetings/" + token));
	}
     //MeetingList
    @GetMapping("/meetinglist")
    public List<MeetingListResDto> getMeetings(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(required = false) MeetingState state
    ) {

        return meetingListService.getMeetings(user.getUserId(), state);
    }

    // [추가] 초대 코드로 입장 API
    @PostMapping("/meetings/{token}/join")
    public ResponseEntity<UrlResponse> joinMeeting(@AuthenticationPrincipal CustomUserDetails user,
                                              @PathVariable("token") String token) {
        Long meetingId = meetingService.joinMeeting(user.getUserId(), token);
        return ResponseEntity.status(HttpStatus.CREATED).body(new UrlResponse("초대코드 생성 ", "/meetings/" + meetingId));
    }
}