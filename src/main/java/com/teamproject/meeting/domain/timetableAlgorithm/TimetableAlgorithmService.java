package com.teamproject.meeting.domain.timetableAlgorithm;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.teamproject.meeting.port.MeetingRepositoryPort;
import com.teamproject.meeting.port.MeetingVoteCandidateRepositoryPort;

@Service
public class TimetableAlgorithmService {
	
	private final MeetingRepositoryPort meetingRepositoryPort;
	private final MeetingVoteCandidateRepositoryPort meetingVoteCandidateRepositoryPort;
	
	public TimetableAlgorithmService(MeetingRepositoryPort meetingRepositoryPort, MeetingVoteCandidateRepositoryPort meetingVoteCandidateRepositoryPort) {
		this.meetingRepositoryPort = meetingRepositoryPort;
		this.meetingVoteCandidateRepositoryPort = meetingVoteCandidateRepositoryPort;
	}
	
	public void TimetableAlgorithm(Long meetingId) {
		
		// timeList 받아오기
		List<TimetableAlgorithmQueryDto> timeList = meetingRepositoryPort.getSortTimetable(meetingId);
		
		// 알고리즘
		// - participant 기준 그룹핑
		Map<Long, List<TimetableAlgorithmQueryDto>> groupedMap = new LinkedHashMap<>();
		
		for(TimetableAlgorithmQueryDto t : timeList) {
			if(t.getStartTime() == null) continue;
			
			if(!groupedMap.containsKey(t.getMeetingParticipantId())) {
				groupedMap.put(t.getMeetingParticipantId(), new ArrayList<>());
			}
			groupedMap.get(t.getMeetingParticipantId()).add(t);
		}
		
		// - 시간표 등록을 아무도 안한 경우
		if(groupedMap.isEmpty()) {
			return;
		}
		
		// - 첫 번째 참여자 구간으로 초기화
		List<CandidateDto> candidates = new ArrayList<>();
		List<TimetableAlgorithmQueryDto> firstParticipant = new ArrayList<>(groupedMap.values()).get(0); // 첫번째 사람의 List를 들고온다.
		
		for(TimetableAlgorithmQueryDto t : firstParticipant) {
			candidates.add(new CandidateDto(t.getStartTime(), t.getEndTime()));
		}
		
		// - 나머지 참여자들과 교집합 누적
		boolean isFirst = true;
		for(List<TimetableAlgorithmQueryDto> participantSlots : groupedMap.values()) {
			// 첫 번째 참여자는 skip
			if(isFirst) {
				isFirst = false;
				continue;
			}
			
			List<CandidateDto> newCandidates = new ArrayList<>();
			
			for(CandidateDto candidate : candidates) {
				for(TimetableAlgorithmQueryDto t : participantSlots) {
					// 곂치는 구간 계산
					LocalDateTime start = candidate.getStartTime().isAfter(t.getStartTime()) ? candidate.getStartTime() : t.getStartTime();
					LocalDateTime end = candidate.getEndTime().isBefore(t.getEndTime()) ? candidate.getEndTime() : t.getEndTime();
					
					// 유효한 구간만 추가
					if(start.isBefore(end)) {
						newCandidates.add(new CandidateDto(start, end));
					}
				}
			}
			
			candidates = newCandidates;
			
			if(candidates.isEmpty()) {
				return;
			}
		}
		
		// candidate에 등록
		meetingVoteCandidateRepositoryPort.createVoteCandidate(meetingId, candidates);
		
	}
}
