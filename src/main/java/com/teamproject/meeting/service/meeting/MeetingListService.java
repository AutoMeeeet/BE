package com.teamproject.meeting.service.meeting;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.teamproject.meeting.dto.meeting.MeetingListReqDto;
import com.teamproject.meeting.dto.meeting.MeetingListResDto;
import com.teamproject.meeting.enums.meeting.MeetingState;
import com.teamproject.meeting.port.MeetingRepositoryPort;

@Service
public class MeetingListService {

    private final MeetingRepositoryPort meetingRepositoryPort;

    public MeetingListService(MeetingRepositoryPort meetingRepositoryPort) {
        this.meetingRepositoryPort = meetingRepositoryPort;
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