package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.pojo.dto.table.EventDto;
import org.example.pojo.dto.update.EventUpdateDto;
import org.example.services.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @GetMapping("/event")
    public ResponseEntity<List<EventDto>> getEventList() {
        return ResponseEntity.ok(eventService.getEventList());
    }

    @GetMapping("/event/{centerId}")
    public ResponseEntity<List<EventDto>> getCenterEventList(@PathVariable Long centerId) {
        return ResponseEntity.ok(eventService.getCenterEventList(centerId));
    }

    @PostMapping("/event")
    public ResponseEntity<Long> addEvent(@RequestBody EventDto eventDto) {
        return ResponseEntity.ok(eventService.addEvent(eventDto));
    }

    @PatchMapping("/event/{id}")
    public ResponseEntity<Long> updateEvent(@PathVariable Long id, @RequestBody EventUpdateDto updateDto) {
        return ResponseEntity.ok(eventService.updateEvent(id, updateDto));
    }

    @DeleteMapping("/event/{id}")
    public ResponseEntity<Long> deleteEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.deleteEvent(id));
    }
}
