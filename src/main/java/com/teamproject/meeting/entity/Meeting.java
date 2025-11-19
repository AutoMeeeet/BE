package com.teamproject.meeting.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Meeting {
    private Long meetingId;
    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String meetingState;
    private String locationType;
    private String location;
    private LocalDate date;
    private String meetingUrl;
}