package org.example.mapper;

import lombok.RequiredArgsConstructor;
import org.example.entities.Event;
import org.example.entities.Volunteer;
import org.example.enums.EParticipant;
import org.example.exceptions.CenterNotFoundException;
import org.example.pojo.dto.table.CenterParticipantDto;
import org.example.pojo.dto.table.DistrictParticipantDto;
import org.example.pojo.dto.table.EventParticipantDto;
import org.example.pojo.dto.table.LinkDto;
import org.example.pojo.dto.table.VolunteerDto;
import org.example.pojo.dto.update.ParticipantUpdateDto;
import org.example.repositories.CenterRepository;
import org.example.utils.DateUtil;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ParticipialMapper {
    private final LinkMapper linkMapper;
    private final CenterRepository centerRepository;

    public VolunteerDto volunteerDto(Volunteer volunteer) {
        VolunteerDto volunteerDto = new VolunteerDto();
        volunteerDto.setId(volunteer.getVolunteerId());
        volunteerDto.setFullName(volunteer.getFullName());
        volunteerDto.setBirthday(DateUtil.birthdayWithAge(volunteer.getBirthday()));
        volunteerDto.setTgLink(volunteer.getTgLink());
        volunteerDto.setVk(volunteer.getVk());
        volunteerDto.setColor(volunteer.getColor().getValue());
        volunteerDto.setEventLinkList(eventLinkList(volunteer.getEventList()));
        volunteerDto.setComment(volunteer.getComment());
        volunteerDto.setRank(volunteer.getRank());
        volunteerDto.setHasInterview(volunteer.isHasInterview());
        volunteerDto.setLevel(volunteer.getLevel());
        volunteerDto.setCenterLink(linkMapper.center(volunteer.getCenter()));
        return volunteerDto;
    }

    public DistrictParticipantDto districtParticipantDto(Volunteer volunteer) {
        DistrictParticipantDto districtParticipantDto = new DistrictParticipantDto();
        districtParticipantDto.setId(volunteer.getVolunteerId());
        districtParticipantDto.setFullName(volunteer.getFullName());
        districtParticipantDto.setBirthday(DateUtil.birthdayWithAge(volunteer.getBirthday()));
        districtParticipantDto.setTgLink(volunteer.getTgLink());
        districtParticipantDto.setVkLink(volunteer.getVk());
        districtParticipantDto.setColor(volunteer.getColor().getValue());
        districtParticipantDto.setEventLinkList(eventLinkList(volunteer.getEventList()));
        return districtParticipantDto;
    }

    public EventParticipantDto eventParticipantDto(Volunteer volunteer) {
        EventParticipantDto eventParticipantDto = new EventParticipantDto();
        eventParticipantDto.setId(volunteer.getVolunteerId());
        eventParticipantDto.setFullName(volunteer.getFullName());
        eventParticipantDto.setBirthday(DateUtil.birthdayWithAge(volunteer.getBirthday()));
        eventParticipantDto.setTgLink(volunteer.getTgLink());
        eventParticipantDto.setFunctional(EParticipant.VOLUNTEER);
        eventParticipantDto.setTesting(volunteer.isTesting());
        eventParticipantDto.setComment(volunteer.getComment());
        eventParticipantDto.setRank(volunteer.getRank());
        eventParticipantDto.setHasClothes(volunteer.getHasAnorak()); // TODO : так ли понял
        return eventParticipantDto;
    }

    public CenterParticipantDto centerParticipantDto(Volunteer volunteer) {
        CenterParticipantDto centerParticipantDto = new CenterParticipantDto();
        centerParticipantDto.setId(volunteer.getVolunteerId());
        centerParticipantDto.setFullName(volunteer.getFullName());
        centerParticipantDto.setBirthday(volunteer.getBirthday());
        centerParticipantDto.setTgLink(volunteer.getTgLink());
        centerParticipantDto.setVkLink(volunteer.getVk());
        centerParticipantDto.setColor(volunteer.getColor());
        centerParticipantDto.setRank(volunteer.getRank());
        centerParticipantDto.setInterview(volunteer.isHasInterview());
        centerParticipantDto.setLevel(volunteer.getLevel());
        return centerParticipantDto;
    }

    public Volunteer volunteer(Volunteer volunteer, ParticipantUpdateDto updateDto) {
        if (updateDto.getColor() != null) {
            volunteer.setColor(updateDto.getColor());
        }

        if (updateDto.getHasInterview() != null) {
            volunteer.setHasInterview(updateDto.getHasInterview());
        }

        if (updateDto.getLevel() != null) {
            volunteer.setLevel(updateDto.getLevel());
        }

        if (updateDto.getCenterId() != null) {
            volunteer.setCenter(centerRepository.findById(updateDto.getCenterId())
                    .orElseThrow(() -> new CenterNotFoundException(updateDto.getCenterId().toString()))
            );
        }

        return volunteer;
    }

    private List<LinkDto> eventLinkList(List<Event> eventList) {
        return eventList.stream().map(linkMapper::event).toList();
    }
}
