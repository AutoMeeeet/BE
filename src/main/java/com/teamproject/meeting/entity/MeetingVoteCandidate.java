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
public class MeetingVoteCandidate {
	private Long meetingCandidateId;
	private Long meetingId;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
}
