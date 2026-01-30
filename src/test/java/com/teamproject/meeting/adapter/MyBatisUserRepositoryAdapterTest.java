package com.teamproject.meeting.adapter;

import com.teamproject.meeting.entity.Users;
import com.teamproject.meeting.enums.users.Provider;
import com.teamproject.meeting.enums.users.Role;
import com.teamproject.meeting.port.UsersRepositoryPort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:schema-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class MyBatisUserRepositoryAdapterTest {

    private final UsersRepositoryPort usersRepositoryPort;
    
    @Autowired
    MyBatisUserRepositoryAdapterTest(UsersRepositoryPort usersRepositoryPort) {
        this.usersRepositoryPort = usersRepositoryPort;
    }

    @Test
    void existsByEmail_저장된이메일이면_true반환() {
        // given
    	Users user = Users.builder()
                .email("test@test.com")
                .password("1234")
                .nickname("홍길동")
                .provider(Provider.LOCAL)
                .role(Role.USERS)
                .build();

        usersRepositoryPort.saveUsers(user);

        // when
        boolean exists = usersRepositoryPort.existsByEmail("test@test.com");

        // then
        assertTrue(exists);
    }

    @Test
    void findByEmail_저장된유저를_정상조회한다() {
        // given
    	Users user = Users.builder()
                .email("bbb@test.com")
                .password("9999")
                .nickname("철수")
                .provider(Provider.GOOGLE)
                .role(Role.USERS)
                .build();

        usersRepositoryPort.saveUsers(user);

        // when
        Users result = usersRepositoryPort.findByEmail("bbb@test.com");

        // then
        assertNotNull(result);
        assertEquals("bbb@test.com", result.getEmail());
        assertEquals("9999", result.getPassword());
        assertEquals(Role.USERS, result.getRole());
    }
    
    @Test
    void findPasswordHashByUserId_비밀번호를_정상조회한다() {

    	// given
        Long userId = 1L; // seed data 기준

        // when
        String passwordHash = usersRepositoryPort.findPasswordHashByUserId(userId);

        // then
        assertNotNull(passwordHash);
        assertEquals("pw", passwordHash);
    }
    
    @Test
    void changePW_비밀번호가_정상적으로_변경된다() {

    	Users user = Users.builder()
                .email("change@test.com")
                .password("oldPassword")
                .nickname("테스터2")
                .provider(Provider.LOCAL)
                .role(Role.USERS)
                .build();
        
        usersRepositoryPort.saveUsers(user);
        
        Long generatedId = user.getUserId();
        String newEncodedPW = "newEncodedPassword";

        usersRepositoryPort.changePW(generatedId, newEncodedPW);

        String updatedPW = usersRepositoryPort.findPasswordHashByUserId(generatedId);
        assertNotNull(updatedPW);
        assertEquals(newEncodedPW, updatedPW);
    }
    
    @Test
    void USERS_데이터가_존재한다() {
        assertTrue(usersRepositoryPort.existsByEmail("test@test.com"));
    }
}
