package org.example.mapper;

import org.example.entities.Event;
import org.example.pojo.dto.EventDto;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {
    public EventDto eventDto(Event event) {
        EventDto eventDto = new EventDto();
        eventDto.setId(event.getId());
        eventDto.setName(event.getName());
        eventDto.setStartTime(event.getStartTime());
        eventDto.setEndTime(event.getEndTime());
        eventDto.setLocation(event.getLocation());
        eventDto.setTeamLeader(event.getTeamLeader());
        return eventDto;
    }

    public Event event(EventDto eventDto) {
        Event event = new Event();
        event.setName(eventDto.getName());
        event.setStartTime(eventDto.getStartTime());
        event.setEndTime(eventDto.getEndTime());
        event.setLocation(eventDto.getLocation());
        event.setTeamLeader(eventDto.getTeamLeader());
        return event;
    }
}
