package com.teamproject.meeting.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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