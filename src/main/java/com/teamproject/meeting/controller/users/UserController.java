package com.teamproject.meeting.controller.users;


import com.teamproject.meeting.dto.users.JoinDto;
import com.teamproject.meeting.dto.users.MyPageDto;
import com.teamproject.meeting.dto.users.NickNameDto;
import com.teamproject.meeting.dto.users.PWDto;
import com.teamproject.meeting.infrastructure.common.CommonResponse;
import com.teamproject.meeting.infrastructure.security.local.CustomUserDetails;
import com.teamproject.meeting.service.users.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {


    private final UserService userService;
    public UserController( UserService userService) {

        this.userService = userService;
    }

    @PutMapping("/nickname")
    public ResponseEntity<CommonResponse> changeNickname(@AuthenticationPrincipal CustomUserDetails principal, @Valid @RequestBody NickNameDto dto) {
        userService.changeNickname(dto.getNickname(), principal.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponse("이름 변경 성공"));
    }

    @PutMapping("/password")
    public ResponseEntity<CommonResponse> changePW(@AuthenticationPrincipal CustomUserDetails principal, @Valid @RequestBody PWDto dto) {
      userService.changePW(principal.getUserId(), dto.getCurrentPW(), dto.getNewPW());
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponse("PW 변경 성공"));
    }

    @PostMapping("/join")
    public ResponseEntity<CommonResponse> joinProcess(@RequestBody JoinDto joinDto) {

        userService.joinProcess(joinDto);

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
