package com.teamproject.meeting.service.meeting;

import com.teamproject.meeting.dto.meeting.MeetingListReqDto;
import com.teamproject.meeting.dto.meeting.MeetingListResDto;
import com.teamproject.meeting.enums.meeting.MeetingState;
import com.teamproject.meeting.port.MeetingRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    @DisplayName("상태(state)를 전달하면 포트로 해당 상태가 그대로 전달된다")
    void getMeetings_state전달확인() {
        // given
        Long userId = 1L;
        MeetingState state = MeetingState.CONFIRMED;
        // Mock 데이터 준비 (빈 리스트 반환 설정)
        when(meetingRepositoryPort.getMeetings(userId, state)).thenReturn(List.of());

        // when
        meetingListService.getMeetings(userId, state);

        // then
        // 포트가 정확한 파라미터로 호출되었는지 검증
        verify(meetingRepositoryPort, times(1)).getMeetings(userId, state);
    }

    @Test
    @DisplayName("서로 다른 날짜의 회의들은 날짜별로 그룹화되어 반환된다")
    void getMeetings_날짜별그룹화_테스트() {
        // given
        Long userId = 1L;
        LocalDate date1 = LocalDate.of(2034, 12, 30);
        LocalDate date2 = LocalDate.of(2034, 12, 31);

        // 12월 30일 회의 2개, 12월 31일 회의 1개 생성
        MeetingListReqDto m1 = createMeeting("회의1", date1.atTime(10, 0));
        MeetingListReqDto m2 = createMeeting("회의2", date1.atTime(14, 0));
        MeetingListReqDto m3 = createMeeting("회의3", date2.atTime(11, 0));

        when(meetingRepositoryPort.getMeetings(userId, MeetingState.CONFIRMED)).thenReturn(Arrays.asList(m1, m2, m3));

        // when
        List<MeetingListResDto> result = meetingListService.getMeetings(userId, MeetingState.CONFIRMED);

        // then
        assertNotNull(result);
        assertEquals(2, result.size()); // 30일, 31일 총 2그룹

        // 첫 번째 그룹 (12월 30일) 검증
        assertEquals(date1, result.get(0).getDate());
        assertEquals(2, result.get(0).getMeetings().size());

        // 두 번째 그룹 (12월 31일) 검증
        assertEquals(date2, result.get(1).getDate());
        assertEquals(1, result.get(1).getMeetings().size());
    }

    @Test
    @DisplayName("조회된 회의가 없으면 빈 리스트를 반환한다")
    void getMeetings_결과없음_테스트() {
        // given
        Long userId = 1L;
        when(meetingRepositoryPort.getMeetings(userId, null)).thenReturn(List.of());

        // when
        List<MeetingListResDto> result = meetingListService.getMeetings(userId, null);

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // 테스트용 DTO 생성을 위한 편의 메서드
    private MeetingListReqDto createMeeting(String title, LocalDateTime startTime) {
        MeetingListReqDto dto = new MeetingListReqDto();
        dto.setTitle(title);
        dto.setStartTime(startTime);
        dto.setMeetingState(MeetingState.CONFIRMED);
        return dto;
    }
}