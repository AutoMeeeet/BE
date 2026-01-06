package com.teamproject.meeting.service.meeting;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.teamproject.meeting.dto.meeting.MeetingListReqDto;
import com.teamproject.meeting.dto.meeting.MeetingListResDto;
import com.teamproject.meeting.infrastructure.redis.RedisUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamproject.meeting.dto.meeting.CreateMeetingDto;
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
	public String createMeeting(Long userId, CreateMeetingDto dto) {
        String token = UUID.randomUUID().toString();
		Meeting meeting = new Meeting();
	    meeting.setTitle(dto.getTitle());
	    meeting.setStartTime(dto.getStartTime());
	    meeting.setLocation(dto.getLocation());
	    meeting.setLocationType(dto.getLocationType());
	    meeting.setCapacity(dto.getCapacity());
	    meeting.setMeetingState(MeetingState.PENDING);
	    meeting.setToken(token);
	    meeting.setInviteExpiresAt(LocalDateTime.now().plusDays(7));
	    
	    meetingRepositoryPort.createMeeting(meeting);
	    
	    Long meetingId = meeting.getMeetingId();
        redisUtil.saveInvitationCode(token, meetingId, 7L * 24 * 60 * 60 * 1000);
		meetingParticipantRepositoryPort.createMeeting(meetingId, userId, dto.getEmailNotification(), Role.ORGANIZER, Permission.AUTHORIZATION, false, false);
		meetingReferenceRepositoryPort.createMeeting(meetingId, dto.getReferenceUrl());
		minutesRepositoryPort.createMeeting(meetingId, dto.getMinutesUrl());
		
		return meeting.getToken();
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
    public List<MeetingListResDto> getMeetings(
            Long userId,
            MeetingState state
    ) {
        if (state == null) {
            state = MeetingState.CONFIRMED;
        }

        List<MeetingListReqDto> flatMeetings = meetingRepositoryPort.getMeetings(userId, state);

        Map<LocalDate, List<MeetingListReqDto>> groupedMap = flatMeetings.stream()
                .collect(Collectors.groupingBy(
                        meeting -> meeting.getStartTime().toLocalDate(),
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        return groupedMap.entrySet().stream()
                .map(entry -> new MeetingListResDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
