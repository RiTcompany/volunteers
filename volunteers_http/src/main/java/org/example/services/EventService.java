package org.example.services;

import org.example.pojo.dto.table.EventDto;
import org.example.pojo.dto.update.EventUpdateDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EventService {
    List<EventDto> getEventList();

    List<EventDto> getCenterEventList(Long centerId);

    Long addEvent(EventDto eventDto);

    Long updateEvent(Long id, EventUpdateDto updateDto);

    Long deleteEvent(Long id);
}
