package com.teamproject.meeting.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import com.teamproject.meeting.entity.Meeting;
import com.teamproject.meeting.enums.meeting.LocationType;
import com.teamproject.meeting.enums.meeting.MeetingState;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "classpath:schema-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class MeetingMapperTest {

    @Autowired
    private MeetingMapper meetingMapper;

    @Test
    @DisplayName("회의 정보를 저장하면 DB에서 생성된 자동 증가 ID가 객체에 채워져야 한다")
    void createMeeting_ShouldPopulateId() {
        // given: 테스트용 Meeting 객체 생성
    	Meeting meeting = Meeting.builder()
    	        .title("MyBatis 테스트 회의")
    	        .startTime(LocalDateTime.now())
    	        .locationType(LocationType.ONLINE)
    	        .location("줌 링크")
    	        .capacity(5)
    	        .token(UUID.randomUUID().toString())
    	        .inviteExpiresAt(LocalDateTime.now().plusDays(7))
    	        .meetingState(MeetingState.PENDING)
    	        .meetingUrl("https://zoom.us/test-link")
    	        .build();
        
        // when: 매퍼 호출 (DB에 INSERT 실행)
        meetingMapper.createMeeting(meeting);

        // then: DB에서 생성된 ID가 meeting 객체에 주입되었는지 검증
        // 1. ID가 null이 아니어야 함
        assertThat(meeting.getMeetingId()).isNotNull();
        // 2. ID가 0보다 큰 값(자동 생성된 값)이어야 함
        assertThat(meeting.getMeetingId()).isPositive();
        
        System.out.println("생성된 Meeting ID: " + meeting.getMeetingId());
    }
}