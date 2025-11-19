package com.teamproject.meeting.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Minutes {
    private Long minutesId;
    private Long meetingId;
    private String minutesUrl;
}