package org.example.mapper;

import org.example.entities.Event;
import org.example.pojo.dto.EventDto;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {
    public EventDto eventDto(Event event) {
        EventDto eventDto = new EventDto();
        eventDto.setName(event.getName());
        eventDto.setStartTime(event.getStartTime());
        eventDto.setEndTime(event.getEndTime());
        eventDto.setLocation(event.getLocation());
        eventDto.setTeamLeader(event.getTeamLeader());
        return eventDto;
    }
}
