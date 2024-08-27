package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.mapper.EventMapper;
import org.example.pojo.dto.EventDto;
import org.example.repositories.EventRepository;
import org.example.services.EventService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Override
    public List<EventDto> getEventList() {
        return eventRepository.findAll().stream().map(eventMapper::eventDto).toList();
    }

    @Override
    public List<EventDto> getCenterEventList(long centerId) {
        return eventRepository.findAllByCenterId(centerId).stream().map(eventMapper::eventDto).toList();
    }
}
