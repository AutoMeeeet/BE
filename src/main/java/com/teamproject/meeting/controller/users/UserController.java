package com.teamproject.meeting.controller.users;


import com.teamproject.meeting.dto.users.JoinDto;
import com.teamproject.meeting.dto.users.MyPageDto;
import com.teamproject.meeting.dto.users.NickNameDto;
import com.teamproject.meeting.dto.users.PWDto;
import com.teamproject.meeting.infrastructure.common.CommonResponse;
import com.teamproject.meeting.infrastructure.security.local.CustomUserDetails;
import com.teamproject.meeting.service.users.JoinService;
import com.teamproject.meeting.service.users.NickNameService;
import com.teamproject.meeting.service.users.PWService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final NickNameService nickNameService;
    private final PWService pwService;
    private final JoinService joinService;

    public UserController(NickNameService nickNameService, PWService pwService, JoinService joinService) {
        this.nickNameService = nickNameService;
        this.pwService = pwService;
        this.joinService = joinService;
    }

    @PutMapping("/nickname")
    public ResponseEntity<CommonResponse> changeNickname(@AuthenticationPrincipal CustomUserDetails principal, @Valid @RequestBody NickNameDto dto) {
        nickNameService.changeNickname(dto.getNickname(), principal.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponse("이름 변경 성공"));
    }

    @PutMapping("/password")
    public ResponseEntity<CommonResponse> changePW(@AuthenticationPrincipal CustomUserDetails principal, @Valid @RequestBody PWDto dto) {
        pwService.changePW(principal.getUserId(), dto.getCurrentPW(), dto.getNewPW());
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponse("PW 변경 성공"));
    }

    @PostMapping("/join")
    public ResponseEntity<CommonResponse> joinProcess(@RequestBody JoinDto joinDto) {

        joinService.joinProcess(joinDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResponse("회원가입 성공"));
    }

    @GetMapping("/mypage")
    public MyPageDto getMyPage(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return new MyPageDto(
                user.getNickname(),
                user.getUsername(), // email
                user.getProvider()
        );
    }
}
