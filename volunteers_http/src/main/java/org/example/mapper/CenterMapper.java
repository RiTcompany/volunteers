package org.example.mapper;

import lombok.RequiredArgsConstructor;
import org.example.entities.Center;
import org.example.pojo.dto.table.CenterDto;
import org.example.repositories.VolunteerRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CenterMapper {
    private final VolunteerRepository volunteerRepository;

    public CenterDto centerDto(Center center) {
        CenterDto centerDto = new CenterDto();
        centerDto.setId(center.getId());
        centerDto.setName(center.getName());
        centerDto.setParticipantCount(volunteerRepository.countAllByCenterId(center.getId()));
        centerDto.setLocation(center.getLocation());
        centerDto.setContact(center.getContact());
        return centerDto;
    }

    public Center center(CenterDto centerDto) {
        Center center = new Center();
        center.setName(centerDto.getName());
        center.setLocation(centerDto.getLocation());
        center.setContact(centerDto.getContact());
        return center;
    }
}
