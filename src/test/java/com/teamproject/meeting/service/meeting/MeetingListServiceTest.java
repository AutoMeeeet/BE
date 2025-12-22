package com.teamproject.meeting.service.meeting;

import com.teamproject.meeting.dto.meeting.MeetingListDto;
import com.teamproject.meeting.enums.meeting.MeetingState;
import com.teamproject.meeting.enums.meeting.MeetingTimeType;
import com.teamproject.meeting.port.MeetingRepositoryPort;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MeetingListServiceTest {

    private final MeetingRepositoryPort meetingRepositoryPort;
    private final MeetingListService meetingListService;

    MeetingListServiceTest() {
        this.meetingRepositoryPort = mock(MeetingRepositoryPort.class);
        this.meetingListService = new MeetingListService(meetingRepositoryPort);
    }

    @Test
    @DisplayName("timeType이 null인 경우 UPCOMING으로 기본 설정된다")
    void getMeetings_timeType이null이면_UPCOMING으로설정() {
        // given
        Long userId = 1L;
        MeetingState state = MeetingState.CONFIRMED;
        List<MeetingListDto> expectedMeetings = Arrays.asList(new MeetingListDto());
        
        when(meetingRepositoryPort.getMeetings(userId, state, MeetingTimeType.UPCOMING))
                .thenReturn(expectedMeetings);

        // when
        List<MeetingListDto> result = meetingListService.getMeetings(userId, state, null);

        // then
        assertEquals(expectedMeetings, result);
        verify(meetingRepositoryPort, times(1)).getMeetings(userId, state, MeetingTimeType.UPCOMING);
    }

    @Test
    @DisplayName("timeType이 UPCOMING인 경우 그대로 전달된다")
    void getMeetings_timeType이UPCOMING이면_그대로전달() {
        // given
        Long userId = 1L;
        MeetingState state = null;
        List<MeetingListDto> expectedMeetings = Arrays.asList(new MeetingListDto());
        
        when(meetingRepositoryPort.getMeetings(userId, state, MeetingTimeType.UPCOMING))
                .thenReturn(expectedMeetings);

        // when
        List<MeetingListDto> result = meetingListService.getMeetings(userId, state, MeetingTimeType.UPCOMING);

        // then
        assertEquals(expectedMeetings, result);
        verify(meetingRepositoryPort, times(1)).getMeetings(userId, state, MeetingTimeType.UPCOMING);
    }

    @Test
    @DisplayName("timeType이 PAST인 경우 그대로 전달된다")
    void getMeetings_timeType이PAST면_그대로전달() {
        // given
        Long userId = 1L;
        MeetingState state = MeetingState.FINISHED;
        List<MeetingListDto> expectedMeetings = Arrays.asList(new MeetingListDto());
        
        when(meetingRepositoryPort.getMeetings(userId, state, MeetingTimeType.PAST))
                .thenReturn(expectedMeetings);

        // when
        List<MeetingListDto> result = meetingListService.getMeetings(userId, state, MeetingTimeType.PAST);

        // then
        assertEquals(expectedMeetings, result);
        verify(meetingRepositoryPort, times(1)).getMeetings(userId, state, MeetingTimeType.PAST);
    }

    @Test
    @DisplayName("state가 null이면 모든 상태의 회의가 조회된다")
    void getMeetings_state가null이어도_정상동작() {
        // given
        Long userId = 1L;
        List<MeetingListDto> expectedMeetings = Arrays.asList(new MeetingListDto());
        
        when(meetingRepositoryPort.getMeetings(userId, null, MeetingTimeType.UPCOMING))
                .thenReturn(expectedMeetings);

        // when
        List<MeetingListDto> result = meetingListService.getMeetings(userId, null, null);

        // then
        assertEquals(expectedMeetings, result);
        verify(meetingRepositoryPort, times(1)).getMeetings(userId, null, MeetingTimeType.UPCOMING);
    }
}