package org.example.services;

import org.example.pojo.dto.EventDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EventService {
    List<EventDto> getEventList();

    List<EventDto> getCenterEventList(long centerId);
}
