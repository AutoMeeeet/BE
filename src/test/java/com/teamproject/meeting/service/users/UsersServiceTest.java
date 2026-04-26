package com.teamproject.meeting.service.users;

import com.teamproject.meeting.dto.users.JoinReqDto;
import com.teamproject.meeting.dto.users.PwReqDto;
import com.teamproject.meeting.entity.Users;
import com.teamproject.meeting.enums.users.Provider;
import com.teamproject.meeting.enums.users.Role;
import com.teamproject.meeting.port.UsersRepositoryPort;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class UsersServiceTest {

    private final UsersRepositoryPort usersRepositoryPort;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UsersService usersService;

    UsersServiceTest() {
        this.usersRepositoryPort = mock(UsersRepositoryPort.class);
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.usersService = new UsersService(usersRepositoryPort, passwordEncoder);
    }

    @Test
    void joinProcess_이미가입된이메일이면_예외발생() {
        // given
        JoinReqDto dto = JoinReqDto.builder()
        		.email("test@test.com")
        		.password("1234")
        		.nickname("홍길동")
        		.build();

        when(usersRepositoryPort.existsByEmail("test@test.com")).thenReturn(true);

        // when & then
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> usersService.joinProcess(dto));

        assertEquals("이미 가입된 이메일입니다.", exception.getMessage());
    }

    @Test
    void joinProcess_정상가입시_유저가저장된다() {
        // given
        JoinReqDto dto = new JoinReqDto("test@test.com", "1234", "홍길동");

        when(usersRepositoryPort.existsByEmail("test@test.com")).thenReturn(false);

        // when
        usersService.joinProcess(dto);

        // then: 저장되는 User 객체를 캡쳐해서 확인
        ArgumentCaptor<Users> captor = ArgumentCaptor.forClass(Users.class);
        verify(usersRepositoryPort, times(1)).saveUsers(captor.capture());

        Users savedUser = captor.getValue();

        assertEquals("test@test.com", savedUser.getEmail());
        assertEquals("홍길동", savedUser.getNickname());
        assertEquals(Provider.LOCAL, savedUser.getProvider());
        assertEquals(Role.USERS, savedUser.getRole());
        assertTrue(passwordEncoder.matches("1234", savedUser.getPassword()));
    }
    
    @Test
    void changePW_현재비밀번호가틀리면_예외발생() {
        // given
        Long userId = 1L;
        String currentPW = "wrongPW";
        String storedPW = passwordEncoder.encode("correctPW");

        PwReqDto dto = new PwReqDto(currentPW, "newPW123");
        
        when(usersRepositoryPort.findPasswordHashByUserId(userId))
                .thenReturn(storedPW);

        // when & then
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class,
                        () -> usersService.changePW(userId, dto));

        assertEquals("현재 비밀번호 불일치", exception.getMessage());

        verify(usersRepositoryPort, never()).changePW(any(), any());
    }

    @Test
    void changePW_현재비밀번호가맞으면_비밀번호변경된다() {
        // given
        Long userId = 1L;
        String currentPW = "oldPW";
        String newPW = "newPW123";

        String storedPW = passwordEncoder.encode(currentPW);

        PwReqDto dto = new PwReqDto(currentPW, newPW);
        
        when(usersRepositoryPort.findPasswordHashByUserId(userId))
                .thenReturn(storedPW);

        // when
        usersService.changePW(userId, dto);

        // then
        verify(usersRepositoryPort, times(1))
                .changePW(eq(userId), argThat(encoded ->
                	passwordEncoder.matches(newPW, encoded)
        ));
    }
}
