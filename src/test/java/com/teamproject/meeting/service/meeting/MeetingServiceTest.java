package com.teamproject.meeting.service.meeting;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.ArgumentMatchers.any;

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
    	
    	// when
    	String token = meetingService.createMeeting(userId, dto);
    	
    	// then
    	assertThat(token).isNotNull();
    	assertThat(token.length()).isGreaterThan(30);  // UUID 확인
    }
}
