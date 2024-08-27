package org.example.mapper;

import lombok.RequiredArgsConstructor;
import org.example.entities.Center;
import org.example.pojo.dto.CenterDto;
import org.example.repositories.VolunteerRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CenterMapper {
    private final VolunteerRepository volunteerRepository;

    public CenterDto centerDto(Center center) {
        CenterDto centerDto = new CenterDto();
        centerDto.setName(center.getName());
        centerDto.setParticipantCount(volunteerRepository.countAllByCenterId(center.getId()));
        centerDto.setLocation(center.getLocation());
        centerDto.setContact(centerDto.getContact());
        return centerDto;
    }
}
