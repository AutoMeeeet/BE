package com.teamproject.meeting.service.meeting;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamproject.meeting.dto.meeting.AllReqDto;
import com.teamproject.meeting.dto.meeting.CreateMeetingReqDto;
import com.teamproject.meeting.dto.meeting.DailyReqDto;
import com.teamproject.meeting.dto.meeting.MainPageResDto;
import com.teamproject.meeting.dto.meeting.MeetingListReqDBDto;
import com.teamproject.meeting.dto.meeting.MeetingListResDto;
import com.teamproject.meeting.dto.meeting.MonthReqDto;
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
	
	public MeetingService(MeetingRepositoryPort meetingRepositoryPort, MeetingParticipantRepositoryPort meetingParticipantRepositoryPort, MeetingReferenceRepositoryPort meetingReferenceRepositoryPort, MinutesRepositoryPort minutesRepositoryPort) {
		this.meetingRepositoryPort = meetingRepositoryPort;
		this.meetingParticipantRepositoryPort = meetingParticipantRepositoryPort;
		this.meetingReferenceRepositoryPort = meetingReferenceRepositoryPort;
		this.minutesRepositoryPort = minutesRepositoryPort;
	}
	
	@Transactional
	public void createMeeting(Long userId, CreateMeetingReqDto dto) {
		Meeting meeting = Meeting.builder()
			.title(dto.getTitle())
		    .startTime(dto.getStartTime().atStartOfDay())
		    .location(dto.getLocation())
		    .locationType(dto.getLocationType())
		    .capacity(dto.getCapacity())
		    .meetingState(MeetingState.PENDING)
		    .token(UUID.randomUUID().toString())
		    .inviteExpiresAt(LocalDateTime.now().plusDays(7))
		    .build();
	    
	    meetingRepositoryPort.createMeeting(meeting);
	    
	    Long meetingId = meeting.getMeetingId();
	    
		meetingParticipantRepositoryPort.createMeeting(meetingId, userId, dto.getEmailNotification(), Role.ORGANIZER, Permission.AUTHORIZATION, false, false);
		meetingReferenceRepositoryPort.createMeeting(meetingId, dto.getReferenceUrl());
		minutesRepositoryPort.createMeeting(meetingId, dto.getMinutesUrl());
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
}
