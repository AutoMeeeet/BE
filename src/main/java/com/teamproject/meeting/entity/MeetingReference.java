package com.teamproject.meeting.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeetingReference {
    private Long referenceId;
    private Long meetingId;
    private String referenceUrl;
}