package com.teamproject.meeting.service.meetingparticipant;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamproject.meeting.domain.timetableAlgorithm.TimetableAlgorithmService;
import com.teamproject.meeting.dto.meetingParticipant.ConfirmVoteReqDto;
import com.teamproject.meeting.dto.meetingParticipant.PermissionUpdateReqDto;
import com.teamproject.meeting.dto.meetingParticipant.TimetableQueryDto;
import com.teamproject.meeting.dto.meetingParticipant.TimetableResDto;
import com.teamproject.meeting.dto.meetingParticipant.TimetableResDto.ParticipantTimetable;
import com.teamproject.meeting.dto.meetingParticipant.TimetableResDto.TimeSlot;
import com.teamproject.meeting.dto.meetingParticipant.VoteResDto;
import com.teamproject.meeting.dto.meetingParticipant.VoteResultResDto;
import com.teamproject.meeting.entity.MeetingAvailability;
import com.teamproject.meeting.port.MeetingParticipantRepositoryPort;
import com.teamproject.meeting.port.MeetingRepositoryPort;
import com.teamproject.meeting.port.MeetingVoteCandidateRepositoryPort;

@Service
public class MeetingParticipantService {

	private final MeetingParticipantRepositoryPort meetingParticipantRepositoryPort;
	private final MeetingRepositoryPort meetingRepositoryPort;
	private final TimetableAlgorithmService timetableAlgorithmService;
	private final MeetingVoteCandidateRepositoryPort meetingVoteCandidateRepositoryPort;
	
	public MeetingParticipantService(MeetingParticipantRepositoryPort meetingParticipantRepositoryPort, MeetingRepositoryPort meetingRepositoryPort, TimetableAlgorithmService timetableAlgorithmService, MeetingVoteCandidateRepositoryPort meetingVoteCandidateRepositoryPort) {
		this.meetingParticipantRepositoryPort = meetingParticipantRepositoryPort;
		this.meetingRepositoryPort = meetingRepositoryPort;
		this.timetableAlgorithmService = timetableAlgorithmService;
		this.meetingVoteCandidateRepositoryPort = meetingVoteCandidateRepositoryPort;
	}
	
	@Transactional(readOnly = true)
	public TimetableResDto getTimetable(Long meetingId, Long userId) {
		
		List<TimetableQueryDto> results = meetingParticipantRepositoryPort.getTimetable(meetingId);
		
		Map<String, ParticipantTimetable> map = new LinkedHashMap<>();
		
		for(TimetableQueryDto result : results) {
			if(!map.containsKey(result.getNickname())) {
				map.put(result.getNickname(), ParticipantTimetable.builder()
					.nickname(result.getNickname())
					.role(result.getRole())
					.isMe(result.getUserId().equals(userId))
					.timetableCase(result.isTimetableCase())
					.timeSlots(new ArrayList<>())
					.build());
			}
			
			if(result.getUserId().equals(userId) || (result.isTimetableCase() && result.getStartTime() != null)) {
				map.get(result.getNickname()).getTimeSlots().add(
					TimeSlot.builder()
					.startTime(result.getStartTime())
					.endTime(result.getEndTime())
					.build());
			}
		}
		
		return TimetableResDto.builder()
				.timetables(new ArrayList<>(map.values()))
				.build();
	}
	
	
	@Transactional
	public void confirmTimetable(Long meetingId, Long userId) {
		
		Long participantId = meetingParticipantRepositoryPort.findParticipantId(meetingId, userId);
		
		// timetable_case = true
		int updatedRows = meetingParticipantRepositoryPort.confirmTimetable(participantId);
		
		if(updatedRows == 0) {
			throw new IllegalStateException("이미 시간표를 등록했슴니다.");
		}
		
		// 확정 미등록 인원 count
		int unconfirmedCount = meetingParticipantRepositoryPort.countUnconfirmedTimetable(meetingId);
		
		// 모두 등록 완료 시
		if(unconfirmedCount == 0) {
			meetingRepositoryPort.updateMeetingStatus(meetingId, 2);
			
			// 시간표 공집합 계산 알고리즘
			timetableAlgorithmService.TimetableAlgorithm(meetingId);
			
			// 이메일 알림
			
		}
	}
	
	@Transactional(readOnly = true)
	public VoteResDto getVotePage(Long meetingId, Long userId) {
		
		List<VoteResDto.VoteCandidate> candidates = meetingParticipantRepositoryPort.getVoteCandidates(meetingId, userId);
		
		// candidates 중 isVote가 true인 게 하나라도 있으면 투표한 것
		boolean hasVoted = false;
		for(VoteResDto.VoteCandidate candidate : candidates) {
			if(candidate.isVote()) {
				hasVoted = true;
				break;
			}
		}
		
		return VoteResDto.builder()
			.hasVoted(hasVoted)
			.candidates(candidates)
			.build();
	}
	
	@Transactional
	public void vote(Long userId, Long meetingId, Long candidateId) {
		
		// insert
		meetingParticipantRepositoryPort.createVoteRecord(userId, meetingId, candidateId);
		
		// 미투표 인원이 없으면 status = 3
		int unvotedCount = meetingParticipantRepositoryPort.countUnvotedParticipant(meetingId);
		
		if(unvotedCount == 0) {
			meetingRepositoryPort.updateMeetingStatus(meetingId, 3);
		}
		
	}
	
	@Transactional(readOnly = true)
	public VoteResultResDto getVoteResult(Long meetingId, Long userId) {
		
		// 방장 여부 확인
		boolean isOrganizer = meetingParticipantRepositoryPort.isOrganizer(meetingId, userId);
		
		if(!isOrganizer) {
			throw new IllegalStateException("방장만 접근 가능합니다.");
		}
		
		List<VoteResultResDto.VoteResult> results = meetingVoteCandidateRepositoryPort.getVoteResult(meetingId);
		
		return VoteResultResDto.builder()
			.results(results)
			.build();
	}
	
	@Transactional
	public void confirmVote(Long meetingId, Long userId, ConfirmVoteReqDto dto) {
		
		// 방장 여부 확인
		boolean isOrganizer = meetingParticipantRepositoryPort.isOrganizer(meetingId, userId);
		
		if(!isOrganizer) {
			throw new IllegalStateException("방장만 접근 가능합니다.");
		}
		
		LocalDateTime startTime = meetingVoteCandidateRepositoryPort.getStartTime(dto.getCandidateId());
		
		LocalDateTime endTime = startTime.plusMinutes(dto.getDuration());
		
		meetingRepositoryPort.confirmMeeting(meetingId, startTime, endTime);
	}
	
	@Transactional
	public void createAvailability(Long meetingId, Long userId, LocalDateTime startTime, LocalDateTime endTime) {
		
		// 참가자 확인
		Long participantId = meetingParticipantRepositoryPort.findParticipantId(meetingId, userId);
		
		if(participantId == null) {
			throw new IllegalArgumentException("해당 회의에 참여 중인 사용자가 아닙니다.");
		}
		
		// timetable_case = false 인 경우 차단하기
		
		// 시간표 저장
		MeetingAvailability availability = MeetingAvailability.builder()
			.meetingParticipantId(participantId)
			.startTime(startTime)
			.endTime(endTime)
			.build();
		
		meetingParticipantRepositoryPort.createAvailability(availability);
	}
	
	@Transactional
	public void deleteAvailability(Long meetingId, Long userId, Long availabilityId) {
		
		// 참가자 확인
		Long participantId = meetingParticipantRepositoryPort.findParticipantId(meetingId, userId);
		
		if(participantId == null) {
			throw new IllegalArgumentException("해당 회의에 참여 중인 사용자가 아닙니다.");
		}
		
		// timetable_case = false 인 경우 차단하기
		
		meetingParticipantRepositoryPort.deleteAvailability(participantId, availabilityId);
	}
	
	@Transactional
	public void updatePermission(Long meetingId, Long userId, Long targetUserId, PermissionUpdateReqDto dto) {
		String permission = meetingParticipantRepositoryPort.findPermission(meetingId, userId);
		
		if(!"AUTHORIZATION".equals(permission)) {
			throw new AccessDeniedException("권한을 부여할 권한이 없습니다.");
		}
		
		meetingParticipantRepositoryPort.updatePermission(meetingId, targetUserId, dto);
	}
	
	@Transactional
	public void leaveMeeting(Long meetingId, Long userId) {
		// 참여 정보 조회 및 검증 (해당 회의에 참여 중인지 확인)
	    Long participantId = meetingParticipantRepositoryPort.findParticipantId(meetingId, userId);
	    
	    if (participantId == null) {
	        throw new IllegalArgumentException("해당 회의의 참여자가 아닙니다.");
	    }

	    // 방장 여부 확인 (방장은 탈퇴 전 위임이 필요함)
	    boolean isOrganizer = meetingParticipantRepositoryPort.isOrganizer(meetingId, userId);
	    if (isOrganizer) {
	        // 비즈니스 정책: 방장은 그냥 나갈 수 없음 (위임 후 탈퇴 혹은 회의 삭제 유도)
	        throw new IllegalStateException("방장은 회의를 탈퇴할 수 없습니다. 다른 사람에게 방장을 위임하세요.");
	    }

	    // 3. 탈퇴 처리 (참여자 레코드 삭제)
	    meetingParticipantRepositoryPort.deleteParticipant(participantId);
	}
}