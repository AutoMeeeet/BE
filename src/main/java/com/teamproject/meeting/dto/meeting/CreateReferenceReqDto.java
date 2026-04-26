package com.teamproject.meeting.dto.meeting;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateReferenceReqDto {
	private String referenceUrl;
}
