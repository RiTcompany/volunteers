package org.example.services;

import org.example.pojo.dto.CenterParticipantDto;
import org.example.pojo.dto.DistrictParticipantDto;
import org.example.pojo.dto.EventParticipantDto;
import org.example.pojo.dto.VolunteerDto;
import org.example.pojo.filters.CenterParticipantFilter;
import org.example.pojo.filters.DistrictParticipantFilter;
import org.example.pojo.filters.EventParticipantFilter;
import org.example.pojo.filters.VolunteerFilter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ParticipantService {
    List<VolunteerDto> getVolunteerList(VolunteerFilter filter);

    Long deleteVolunteer(Long id);

    List<DistrictParticipantDto> getDistrictParticipantList(long districtTeamId, DistrictParticipantFilter filter);

    List<EventParticipantDto> getEventParticipantList(long eventId, EventParticipantFilter filter);

    List<CenterParticipantDto> getCenterParticipantList(long centerId, CenterParticipantFilter filter);
}
