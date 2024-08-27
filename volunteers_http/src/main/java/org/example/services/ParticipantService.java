package org.example.services;

import org.example.pojo.dto.table.CenterParticipantDto;
import org.example.pojo.dto.table.DistrictParticipantDto;
import org.example.pojo.dto.table.EventParticipantDto;
import org.example.pojo.dto.table.VolunteerDto;
import org.example.pojo.dto.update.ParticipantUpdateDto;
import org.example.pojo.filters.ParticipantFilter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ParticipantService {
    List<VolunteerDto> getVolunteerList(ParticipantFilter filter);

    Long updateParticipant(Long volunteerId, ParticipantUpdateDto updateDto);

    Long deleteVolunteer(Long id);

    List<DistrictParticipantDto> getDistrictParticipantList(Long districtTeamId, ParticipantFilter filter);

    List<EventParticipantDto> getEventParticipantList(Long eventId, ParticipantFilter filter);

    List<CenterParticipantDto> getCenterParticipantList(Long centerId, ParticipantFilter filter);
}
