package org.example.services;

import org.example.pojo.dto.DistrictParticipantDto;
import org.example.pojo.dto.EventParticipantDto;
import org.example.pojo.dto.VolunteerDto;
import org.example.pojo.filters.DistrictParticipantFilter;
import org.example.pojo.filters.EventParticipantFilter;
import org.example.pojo.filters.VolunteerFilter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ParticipantService {
    List<VolunteerDto> getVolunteerList(VolunteerFilter filter);

    List<DistrictParticipantDto> getDistrictParticipantList(DistrictParticipantFilter filter);

    List<EventParticipantDto> getEventParticipantList(EventParticipantFilter filter);
}
