package com.teamproject.meeting.adapter;

import com.teamproject.meeting.dto.meeting.MeetingListDto;
import com.teamproject.meeting.enums.meeting.MeetingState;
import com.teamproject.meeting.enums.meeting.MeetingTimeType;
import com.teamproject.meeting.port.MeetingRepositoryPort;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@Import(MyBatisMeetingRepositoryAdapter.class)
@Sql(scripts = "classpath:schema-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@ActiveProfiles("test")
class MyBatisMeetingRepositoryAdapterTest {

    private final MeetingRepositoryPort meetingRepositoryPort;

    @Autowired
    MyBatisMeetingRepositoryAdapterTest(MeetingRepositoryPort meetingRepositoryPort) {
        this.meetingRepositoryPort = meetingRepositoryPort;
    }

    @Test
    void UPCOMING_회의만_조회된다() {
        // given
        Long userId = 1L;

        // when
        List<MeetingListDto> result =
                meetingRepositoryPort.getMeetings(
                        userId,
                        null,
                        MeetingTimeType.UPCOMING
                );

        // then
        assertNotNull(result);

        result.forEach(meeting ->
                assertTrue(meeting.getEndTime().isAfter(LocalDateTime.now()))
        );
    }

    @Test
    void PAST_회의만_조회된다() {
        // given
        Long userId = 1L;

        // when
        List<MeetingListDto> result =
                meetingRepositoryPort.getMeetings(
                        userId,
                        null,
                        MeetingTimeType.PAST
                );

        // then
        assertNotNull(result);

        result.forEach(meeting ->
                assertTrue(meeting.getEndTime().isBefore(LocalDateTime.now())
                        || meeting.getEndTime().isEqual(LocalDateTime.now()))
        );
    }

    @Test
    void CONFIRMED_상태의_회의만_조회된다() {
        // given
        Long userId = 1L;

        // when
        List<MeetingListDto> result =
                meetingRepositoryPort.getMeetings(
                        userId,
                        MeetingState.CONFIRMED,
                        MeetingTimeType.UPCOMING
                );

        // then
        assertNotNull(result);

        result.forEach(meeting ->
                assertEquals(MeetingState.CONFIRMED, meeting.getMeetingState())
        );
    }

    @Test
    void 참가자수가_정상적으로_집계된다() {
        // given
        Long userId = 1L;

        // when
        List<MeetingListDto> result =
                meetingRepositoryPort.getMeetings(
                        userId,
                        null,
                        MeetingTimeType.UPCOMING
                );

        // then
        result.forEach(meeting ->
                assertTrue(meeting.getParticipantCount() >= 0)
        );
    }
}