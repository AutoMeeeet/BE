package com.teamproject.meeting.entity;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeetingAvailability {
    private Long meetingAvailabilityId;
    private Long meetingParticipantId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer weight;
}