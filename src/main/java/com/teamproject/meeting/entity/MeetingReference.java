package com.teamproject.meeting.entity;

<<<<<<< HEAD
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
=======
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
public class MeetingReference {
    private Long referenceId;
    private Long meetingId;
    private String referenceUrl;
}