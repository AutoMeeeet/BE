package com.teamproject.meeting.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeetingParticipant {
    private Long meetingParticipantId;
    private Long meetingId;
    private Long userId;
    private String role; 
    private Boolean emailNotification;
    private String permission;
    private Boolean timetableCase;
    private Boolean voteCase;
}