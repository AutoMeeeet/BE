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
public class MeetingVoteRecord {
	private Long meetingVoteId;
	private Long meetingCandidateId;
	private Long meetingParticipantId;
}
