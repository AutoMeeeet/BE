package com.teamproject.meeting.service.users;

import com.teamproject.meeting.dto.users.JoinDto;
import com.teamproject.meeting.entity.Users;
import com.teamproject.meeting.enums.users.Provider;
import com.teamproject.meeting.enums.users.Role;
import com.teamproject.meeting.port.UsersRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JoinServiceTest {

    private UsersRepositoryPort usersRepositoryPort;
    private BCryptPasswordEncoder passwordEncoder;
    private JoinService joinService;

    @BeforeEach
    void setUp() {
        usersRepositoryPort = mock(UsersRepositoryPort.class);
        passwordEncoder = new BCryptPasswordEncoder();
        joinService = new JoinService(usersRepositoryPort, passwordEncoder);
    }

    @Test
    void joinProcess_이미가입된이메일이면_예외발생() {
        // given
        JoinDto dto = new JoinDto("test@test.com", "1234", "홍길동");

        when(usersRepositoryPort.existsByEmail("test@test.com")).thenReturn(true);

        // when & then
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> joinService.joinProcess(dto));

        assertEquals("이미 가입된 이메일입니다.", exception.getMessage());
    }

    @Test
    void joinProcess_정상가입시_유저가저장된다() {
        // given
        JoinDto dto = new JoinDto("test@test.com", "1234", "홍길동");

        when(usersRepositoryPort.existsByEmail("test@test.com")).thenReturn(false);

        // when
        joinService.joinProcess(dto);

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
}
