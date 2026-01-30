package com.teamproject.meeting.adapter;

import com.teamproject.meeting.dto.meeting.MeetingListReqDBDto;
import com.teamproject.meeting.enums.meeting.MeetingState;
import com.teamproject.meeting.port.MeetingRepositoryPort;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(scripts = "classpath:schema-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@ActiveProfiles("test")
class MyBatisMeetingRepositoryAdapterTest {

    private final MeetingRepositoryPort meetingRepositoryPort;

    @Autowired
    MyBatisMeetingRepositoryAdapterTest(MeetingRepositoryPort meetingRepositoryPort) {
        this.meetingRepositoryPort = meetingRepositoryPort;
    }

    @Test
    @DisplayName("PENDING 상태를 인자로 주면 미확정된 회의만 조회된다")
    void PENDING_회의만_조회된다() {
        // given
        Long userId = 1L;

        // when
        List<MeetingListReqDBDto> result =
                meetingRepositoryPort.getMeetings(userId, MeetingState.PENDING);

        // then
        assertNotNull(result);
        
        result.forEach(meeting ->
                assertEquals(MeetingState.PENDING, meeting.getMeetingState())
        );
    }
}