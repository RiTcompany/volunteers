package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.pojo.dto.CenterParticipantDto;
import org.example.pojo.dto.DistrictParticipantDto;
import org.example.pojo.dto.EventDto;
import org.example.pojo.dto.EventParticipantDto;
import org.example.pojo.dto.VolunteerDto;
import org.example.pojo.filters.CenterParticipantFilter;
import org.example.pojo.filters.DistrictParticipantFilter;
import org.example.pojo.filters.EventParticipantFilter;
import org.example.pojo.filters.VolunteerFilter;
import org.example.services.ParticipantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ParticipantController {
    private final ParticipantService participantService;

    @GetMapping("/volunteer")
    public ResponseEntity<List<VolunteerDto>> getVolunteerList(@RequestBody VolunteerFilter filter) {
        return ResponseEntity.ok(participantService.getVolunteerList(filter));
    }
    @GetMapping("/center_participant/{centerId}")
    public ResponseEntity<List<CenterParticipantDto>> getCenterParticipantList(
            @PathVariable Long centerId, @RequestBody CenterParticipantFilter filter
    ) {
        return ResponseEntity.ok(participantService.getCenterParticipantList(centerId, filter));
    }

    @GetMapping("/event_participant/{eventId}")
    public ResponseEntity<List<EventParticipantDto>> getDistrictParticipantList(
            @PathVariable Long eventId, @RequestBody EventParticipantFilter filter
    ) {
        return ResponseEntity.ok(participantService.getEventParticipantList(eventId, filter));
    }

    @GetMapping("/district_team_participant/{districtTeamId}")
    public ResponseEntity<List<DistrictParticipantDto>> getDistrictParticipantList(
            @PathVariable Long districtTeamId, @RequestBody DistrictParticipantFilter filter
    ) {
        return ResponseEntity.ok(participantService.getDistrictParticipantList(districtTeamId, filter));
    }
}
