package com.teamproject.meeting.dto.meetingParticipant;

import lombok.Getter;

@Getter
public class ConfirmVoteReqDto {
	private Long candidateId;
	private int duration; // 분 단위
}