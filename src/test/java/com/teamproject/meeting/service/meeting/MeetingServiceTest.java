package com.teamproject.meeting.service.meeting;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.teamproject.meeting.enums.meetingparticipant.Permission;
import com.teamproject.meeting.enums.meetingparticipant.Role;
import com.teamproject.meeting.infrastructure.redis.RedisUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.teamproject.meeting.dto.meeting.CreateMeetingDto;
import com.teamproject.meeting.entity.Meeting;
import com.teamproject.meeting.port.MeetingParticipantRepositoryPort;
import com.teamproject.meeting.port.MeetingReferenceRepositoryPort;
import com.teamproject.meeting.port.MeetingRepositoryPort;
import com.teamproject.meeting.port.MinutesRepositoryPort;

@ExtendWith(MockitoExtension.class)
class MeetingServiceTest {

	@Mock private MeetingRepositoryPort meetingRepositoryPort;
	@Mock private MeetingParticipantRepositoryPort meetingParticipantRepositoryPort;
    @Mock private MeetingReferenceRepositoryPort meetingReferenceRepositoryPort;
    @Mock private MinutesRepositoryPort minutesRepositoryPort;
    @Mock private RedisUtil redisUtil;
    @InjectMocks
    private MeetingService meetingService;
    
    @Test
    @DisplayName("회의 생성 시 비즈니스 로직이 정상 작동하고 토큰을 반환한다.")
    void createMeeting_Success() {
    	// given
    	Long userId = 1L;
    	CreateMeetingDto dto = new CreateMeetingDto();
    	dto.setTitle("테스트 회의");
    	dto.setReferenceUrl("http://ref.com");
    	
    	// MyBatis의 useGeneratedKey 동작 시뮬레이션
    	doAnswer(invocation -> {
    		Meeting meeting = invocation.getArgument(0);
    		ReflectionTestUtils.setField(meeting, "meetingId", 100L);  // 가상의 ID 부여
    		return null;
    	}).when(meetingRepositoryPort).createMeeting(any(Meeting.class));
        verify(redisUtil).saveInviteToken(anyString(),eq(100L),anyLong());
    	// when
    	String token = meetingService.createMeeting(userId, dto);
    	
    	// then
    	assertThat(token).isNotNull();
    	assertThat(token.length()).isGreaterThan(30);  // UUID 확인
    }
    @Test
    @DisplayName("유효한 토큰으로 회의 참여 시 참여자로 등록된다.")
    void joinMeeting_Success() {
        Long userId = 2L;
        String validToken  ="valid-token-123";
        Long meetingId = 100L;
        when(redisUtil.getMeetingIdByToken(validToken)).thenReturn(meetingId);
        Long resultMeetingId = meetingService.joinMeeting(userId, validToken);

        assertThat(resultMeetingId).isEqualTo(meetingId);

        verify(meetingParticipantRepositoryPort).createMeeting(
                eq(meetingId),
                eq(userId),
                anyBoolean(),
                eq(Role.PARTICIPANT),
                eq(Permission.WRITE),
                anyBoolean(),
                anyBoolean()
        );
    }
    @Test
    @DisplayName("만료되거나 잘못된 토큰으로 참여 시 예외가 발생한다.")
    void joinMeeting_Fail_InvalidToken() {
        // given
        Long userId = 2L;
        String invalidToken = "invalid-token";

        // Redis 조회 시 null 반환 (토큰 없음)
        when(redisUtil.getMeetingIdByToken(invalidToken)).thenReturn(null);

        // when & then
        assertThatThrownBy(() -> meetingService.joinMeeting(userId, invalidToken))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("유효하지 않거나 만료된 초대 코드입니다.");
    }
}
