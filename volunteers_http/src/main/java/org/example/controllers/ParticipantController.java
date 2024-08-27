package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.pojo.dto.table.CenterParticipantDto;
import org.example.pojo.dto.table.DistrictParticipantDto;
import org.example.pojo.dto.table.EventParticipantDto;
import org.example.pojo.dto.table.VolunteerDto;
import org.example.pojo.dto.update.ParticipantUpdateDto;
import org.example.pojo.filters.ParticipantFilter;
import org.example.services.ParticipantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ParticipantController {
    private final ParticipantService participantService;

    @PostMapping("/volunteer")
    public ResponseEntity<List<VolunteerDto>> getVolunteerList(@RequestBody ParticipantFilter filter) {
        return ResponseEntity.ok(participantService.getVolunteerList(filter));
    }

    @PatchMapping("/volunteer/{id}")
    public ResponseEntity<Long> updateVolunteer(
            @PathVariable Long id, @RequestBody ParticipantUpdateDto updateDto
    ) {
        return ResponseEntity.ok(participantService.updateParticipant(id, updateDto));
    }

    @DeleteMapping("/volunteer/{id}")
    public ResponseEntity<Long> deleteVolunteer(@PathVariable Long id) {
        return ResponseEntity.ok(participantService.deleteVolunteer(id));
    }

    @PostMapping("/center_participant/{centerId}")
    public ResponseEntity<List<CenterParticipantDto>> getCenterParticipantList(
            @PathVariable Long centerId, @RequestBody ParticipantFilter filter
    ) {
        return ResponseEntity.ok(participantService.getCenterParticipantList(centerId, filter));
    }

    @PostMapping("/event_participant/{eventId}")
    public ResponseEntity<List<EventParticipantDto>> getEventParticipantList(
            @PathVariable Long eventId, @RequestBody ParticipantFilter filter
    ) {
        return ResponseEntity.ok(participantService.getEventParticipantList(eventId, filter));
    }

    @PostMapping("/district_team_participant/{districtTeamId}")
    public ResponseEntity<List<DistrictParticipantDto>> getDistrictParticipantList(
            @PathVariable Long districtTeamId, @RequestBody ParticipantFilter filter
    ) {
        return ResponseEntity.ok(participantService.getDistrictParticipantList(districtTeamId, filter));
    }
}
