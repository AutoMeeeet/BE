package com.teamproject.meeting.entity;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MeetingAvailability {
    private Long meetingAvailabilityId;
    private Long meetingParticipantId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer weight;
}