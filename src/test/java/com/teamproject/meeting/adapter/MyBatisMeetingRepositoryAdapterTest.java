package com.teamproject.meeting.adapter;

import com.teamproject.meeting.dto.meeting.MeetingListReqDto;
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
    void CONFIRMED_상태의_회의만_조회된다() {
        // given
        Long userId = 1L;

        // when
        List<MeetingListReqDto> result =
                meetingRepositoryPort.getMeetings(
                        userId,
                        MeetingState.CONFIRMED
                );

        // then
        assertNotNull(result);
        assertFalse(result.isEmpty(), "결과 리스트가 비어있지 않아야한다.");

        result.forEach(meeting ->
                assertEquals(MeetingState.CONFIRMED, meeting.getMeetingState())
        );
    }

    @Test
    @DisplayName("PENDING 상태를 인자로 주면 미확정된 회의만 조회된다")
    void PENDING_회의만_조회된다() {
        // given
        Long userId = 1L;

        // when
        List<MeetingListReqDto> result =
                meetingRepositoryPort.getMeetings(userId, MeetingState.PENDING);

        // then
        assertNotNull(result);
        
        result.forEach(meeting ->
                assertEquals(MeetingState.PENDING, meeting.getMeetingState())
        );
    }

    @Test
    @DisplayName("FINISHED 상태를 인자로 주면 종료된 회의만 조회된다")
    void FINISHED_회의만_조회된다() {
        // given
        Long userId = 1L;

        // when
        List<MeetingListReqDto> result =
                meetingRepositoryPort.getMeetings(userId, MeetingState.FINISHED);

        // then
        assertNotNull(result);
        
        result.forEach(meeting ->
                assertEquals(MeetingState.FINISHED, meeting.getMeetingState())
        );
    }
    
    @Test
    void 참가자수가_정상적으로_집계된다() {
        // given
        Long userId = 1L;

        // when
        List<MeetingListReqDto> result =
                meetingRepositoryPort.getMeetings(
                        userId,
                        null
                );

        // then
        result.forEach(meeting ->
                assertTrue(meeting.getParticipantCount() >= 0)
        );
    }
}