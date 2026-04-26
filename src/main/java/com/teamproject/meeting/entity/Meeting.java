package com.teamproject.meeting.entity;

<<<<<<< HEAD
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
=======
import java.time.LocalDateTime;

import com.teamproject.meeting.enums.meeting.LocationType;
import com.teamproject.meeting.enums.meeting.MeetingState;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
>>>>>>> origin/dev
public class Meeting {
    private Long meetingId;
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
<<<<<<< HEAD
    private String meetingState;
    private String locationType;
    private String location;
    private LocalDate date;
    private String meetingUrl;
=======
    private MeetingState meetingState; // 확정, 미확정, 종료
    private LocationType locationType; // 온라인, 오프라인
    private String location; // 주소 or 비대면
    private String meetingUrl;
    private Integer capacity; // 정원
    private String token;
    private LocalDateTime inviteExpiresAt;
>>>>>>> origin/dev
}