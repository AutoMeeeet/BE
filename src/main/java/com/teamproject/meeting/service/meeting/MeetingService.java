package com.teamproject.meeting.service.meeting;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.teamproject.meeting.dto.meeting.MeetingListResDto;
import com.teamproject.meeting.infrastructure.redis.RedisUtil;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.teamproject.meeting.dto.meeting.AllReqDto;
import com.teamproject.meeting.dto.meeting.CreateMeetingReqDto;
import com.teamproject.meeting.dto.meeting.CreateMinutesReqDto;
import com.teamproject.meeting.dto.meeting.CreateReferenceReqDto;
import com.teamproject.meeting.dto.meeting.DailyReqDto;
import com.teamproject.meeting.dto.meeting.MainPageResDto;
import com.teamproject.meeting.dto.meeting.MeetingListReqDBDto;
import com.teamproject.meeting.dto.meeting.MonthReqDto;
import com.teamproject.meeting.dto.meetingParticipant.MeetingDetailResDto;
import com.teamproject.meeting.dto.meetingParticipant.MeetingUpdateReqDto;
import com.teamproject.meeting.dto.meetingParticipant.StepResDto;
import com.teamproject.meeting.entity.Meeting;
import com.teamproject.meeting.enums.meeting.MeetingState;
import com.teamproject.meeting.enums.meetingparticipant.Permission;
import com.teamproject.meeting.enums.meetingparticipant.Role;
import com.teamproject.meeting.port.MeetingParticipantRepositoryPort;
import com.teamproject.meeting.port.MeetingReferenceRepositoryPort;
import com.teamproject.meeting.port.MeetingRepositoryPort;
import com.teamproject.meeting.port.MinutesRepositoryPort;

@Service
public class MeetingService {
	
	private final MeetingRepositoryPort meetingRepositoryPort;
	private final MeetingParticipantRepositoryPort meetingParticipantRepositoryPort;
	private final MeetingReferenceRepositoryPort meetingReferenceRepositoryPort;
	private final MinutesRepositoryPort minutesRepositoryPort;
	private final RedisUtil redisUtil;
	
	public MeetingService(MeetingRepositoryPort meetingRepositoryPort, MeetingParticipantRepositoryPort meetingParticipantRepositoryPort, MeetingReferenceRepositoryPort meetingReferenceRepositoryPort, MinutesRepositoryPort minutesRepositoryPort, RedisUtil redisUtil) {
		this.meetingRepositoryPort = meetingRepositoryPort;
		this.meetingParticipantRepositoryPort = meetingParticipantRepositoryPort;
		this.meetingReferenceRepositoryPort = meetingReferenceRepositoryPort;
		this.minutesRepositoryPort = minutesRepositoryPort;
        this.redisUtil = redisUtil;
    }
	
	
	@Transactional
	public void createMeeting(Long userId, CreateMeetingReqDto dto) {
		String token = UUID.randomUUID().toString();
		Meeting meeting = Meeting.builder()
				.title(dto.getTitle())
			    .startTime(dto.getStartTime().atStartOfDay())
			    .location(dto.getLocation())
			    .locationType(dto.getLocationType())
			    .capacity(dto.getCapacity())
			    .meetingState(MeetingState.PENDING)
			    .token(token)
			    .inviteExpiresAt(LocalDateTime.now().plusDays(7))
			    .meetingStatus(1)
			    .build();
	    
	    meetingRepositoryPort.createMeeting(meeting);
	    
	    Long meetingId = meeting.getMeetingId();
        redisUtil.saveInvitationCode(token, meetingId, 7L * 24 * 60 * 60 * 1000);
		meetingParticipantRepositoryPort.createMeeting(meetingId, userId, dto.getEmailNotification(), Role.ORGANIZER, Permission.AUTHORIZATION, false, false);
		if(StringUtils.hasText(dto.getReferenceUrl())) {
			meetingReferenceRepositoryPort.createMeeting(meetingId, dto.getReferenceUrl());
		}
		if(StringUtils.hasText(dto.getMinutesUrl())) {
			minutesRepositoryPort.createMeeting(meetingId, dto.getMinutesUrl());
		}
	}
	
	@Transactional(readOnly = true)
	public MainPageResDto.Main getMonthMeetings(Long userId, MonthReqDto date) {
		// 시작일 종료일 범위 (달력)
		LocalDateTime startOfMonth = LocalDate.of(date.getYear(), date.getMonth(), 1).atStartOfDay();
		LocalDateTime endOfMonth = startOfMonth.toLocalDate().withDayOfMonth(startOfMonth.toLocalDate().lengthOfMonth()).atTime(LocalTime.MAX);
		
		List<MainPageResDto.MonthlySummary> summaries = meetingRepositoryPort.getMonthlySummaries(userId, startOfMonth, endOfMonth);
		
		return MainPageResDto.Main.builder()
				.monthlySummaries(summaries)
				.build();
    }
	
	@Transactional(readOnly = true)
	public List<MainPageResDto.MeetingDetail> getDailyMeetings(Long userId, DailyReqDto date) {
		LocalDate selectedDate = LocalDate.of(date.getYear(), date.getMonth(), date.getDay());
		LocalDateTime startOfDay = selectedDate.atStartOfDay();
		LocalDateTime endOfDay = selectedDate.atTime(LocalTime.MAX);
		
		return meetingRepositoryPort.getMeetingDetailsByDate(userId, startOfDay, endOfDay);
	}
	
	@Transactional(readOnly = true)
    public List<MeetingListResDto> getMeetings(
            Long userId,
            AllReqDto all
    ) {
		MeetingState state = (all.getMeetingState() == null) 
                ? MeetingState.CONFIRMED 
                : all.getMeetingState();
    	
        List<MeetingListReqDBDto> flatMeetings = meetingRepositoryPort.getMeetings(userId, state);

        Map<LocalDate, List<MeetingListReqDBDto>> groupedMap = flatMeetings.stream()
                .collect(Collectors.groupingBy(
                        meeting -> meeting.getStartTime().toLocalDate(),
                        LinkedHashMap::new, 
                        Collectors.toList()
                ));

        return groupedMap.entrySet().stream()
                .map(entry -> new MeetingListResDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Transactional
    public Long joinMeeting(Long userId, String token) {
        Long meetingId = redisUtil.getMeetingIdByToken(token);
        if (meetingId == null) {
            throw new IllegalArgumentException("유효하지 않거나 만료된 초대 코드입니다.");
        }

        // 참여자 등록 (기본값: PARTICIPANT / READ_ONLY)
        meetingParticipantRepositoryPort.createMeeting(meetingId, userId, true, Role.PARTICIPANT, Permission.WRITE, false, false);
        return meetingId;
    }
    
    @Transactional(readOnly = true)
    public StepResDto getStep(Long meetingId) {
    	
    	return meetingRepositoryPort.getStep(meetingId);
    }
    
    @Transactional
    public void deleteMeeting(Long meetingId) {
    	
    	meetingRepositoryPort.deleteMeeting(meetingId);
    }
    
    @Transactional(readOnly = true)
    public MeetingDetailResDto getMeetingDetailPage(Long meetingId, Long userId) {
    	
    	// 참여자 검증
    	boolean isParticipant = meetingParticipantRepositoryPort.existByMeetingIdAndUserId(meetingId, userId);
    	
    	if(!isParticipant) {
    		throw new IllegalArgumentException("해당 회의에 접근 권한이 없습니다.");
    	}
    	
    	// 회의 조회
    	MeetingDetailResDto.MeetingInfo meetingInfo = meetingRepositoryPort.getMeetingDetail(meetingId);
    	
    	// 참고자료 조회
    	List<MeetingDetailResDto.Reference> references = meetingReferenceRepositoryPort.findByMeetingId(meetingId);
    	
    	// 회의록 조회
    	List<MeetingDetailResDto.Minutes> minutes = minutesRepositoryPort.findMeetingId(meetingId);
    	
    	// 참여자 조회
    	List<MeetingDetailResDto.Participant> participants = meetingParticipantRepositoryPort.findParticipants(meetingId, userId);
    	
    	// DTO 조립
    	return MeetingDetailResDto.builder()
    		.title(meetingInfo.getTitle())
    		.startTime(meetingInfo.getStartTime())
    		.endTime(meetingInfo.getEndTime())
    		.meetingState(meetingInfo.getMeetingState())
    		.locationType(meetingInfo.getLocationType())
    		.location(meetingInfo.getLocation())
    		.references(references)
    		.minutes(minutes)
    		.participants(participants)
    		.build();
    }
    
    @Transactional
    public void updateMeeting(Long meetingId, Long userId, MeetingUpdateReqDto dto) {
    	String permission = meetingParticipantRepositoryPort.findPermission(meetingId, userId);
    	
    	if(permission == null || "READ".equals(permission)) {
    		throw new AccessDeniedException("회원 정보를 수정할 권한이 없습니다.");
    	}
    	
    	meetingRepositoryPort.updateMeeting(meetingId, dto);
    }
    
    @Transactional
    public void createReference(Long userId, Long meetingId, CreateReferenceReqDto dto) {
    	String permission = meetingParticipantRepositoryPort.findPermission(meetingId, userId);
    	if(permission == null || "READ".equals(permission)) {
    		throw new AccessDeniedException("자료를 관리할 권한이 없습니다.");
    	}
    	
    	meetingReferenceRepositoryPort.createMeeting(meetingId, dto.getReferenceUrl());
    }
    
    @Transactional
    public void deleteReference(Long userId, Long referenceId) {
    	Long meetingId = meetingReferenceRepositoryPort.findMeetingIdByReferenceId(referenceId);
        if (meetingId == null) {
            throw new IllegalArgumentException("해당 자료가 존재하지 않습니다.");
        }
    	
    	String permission = meetingParticipantRepositoryPort.findPermission(meetingId, userId);
    	if(permission == null || "READ".equals(permission)) {
    		throw new AccessDeniedException("자료를 관리할 권한이 없습니다.");
    	}
    	
    	meetingReferenceRepositoryPort.deleteById(referenceId);
    }

    @Transactional
    public void createMinutes(Long userId, Long meetingId, CreateMinutesReqDto dto) {
    	String permission = meetingParticipantRepositoryPort.findPermission(meetingId, userId);
    	if(permission == null || "READ".equals(permission)) {
    		throw new AccessDeniedException("자료를 관리할 권한이 없습니다.");
    	}
    	
    	minutesRepositoryPort.createMeeting(meetingId, dto.getMinutesUrl());
    }
    
    @Transactional
    public void deleteMinutes(Long userId, Long minutesId) {
    	Long meetingId = minutesRepositoryPort.findMeetingIdByMinutesId(minutesId);
        if (meetingId == null) {
            throw new IllegalArgumentException("해당 자료가 존재하지 않습니다.");
        }
    	
    	String permission = meetingParticipantRepositoryPort.findPermission(meetingId, userId);
    	if(permission == null || "READ".equals(permission)) {
    		throw new AccessDeniedException("자료를 관리할 권한이 없습니다.");
    	}
    	
    	minutesRepositoryPort.deleteById(minutesId);
    }
}