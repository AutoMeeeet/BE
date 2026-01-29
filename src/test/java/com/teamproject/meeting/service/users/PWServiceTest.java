package com.teamproject.meeting.service.users;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.teamproject.meeting.port.UsersRepositoryPort;

import static org.mockito.Mockito.*;

class PWServiceTest {

    private final UsersRepositoryPort usersRepositoryPort;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserService userService;

    PWServiceTest(UserService userService) {
        this.userService = userService;
        this.usersRepositoryPort = mock(UsersRepositoryPort.class);
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();

    }

    @Test
    void changePW_현재비밀번호가틀리면_예외발생() {
        // given
        Long userId = 1L;
        String currentPW = "wrongPW";
        String storedPW = bCryptPasswordEncoder.encode("correctPW");

        when(usersRepositoryPort.findPasswordHashByUserId(userId))
                .thenReturn(storedPW);

        // when & then
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class,
                        () -> userService.changePW(userId, currentPW, "newPW"));

        assertEquals("현재 비밀번호 불일치", exception.getMessage());

        verify(usersRepositoryPort, never()).changePW(any(), any());
    }

    @Test
    void changePW_현재비밀번호가맞으면_비밀번호변경된다() {
        // given
        Long userId = 1L;
        String currentPW = "oldPW";
        String newPW = "newPW123";

        String storedPW = bCryptPasswordEncoder.encode(currentPW);

        when(usersRepositoryPort.findPasswordHashByUserId(userId))
                .thenReturn(storedPW);

        // when
        pwService.changePW(userId, currentPW, newPW);

        // then
        verify(usersRepositoryPort, times(1))
                .changePW(eq(userId), argThat(encoded ->
                        bCryptPasswordEncoder.matches(newPW, encoded)
        ));
    }
}