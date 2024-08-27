package org.example.mapper;

import org.example.entities.Center;
import org.example.entities.Event;
import org.example.pojo.dto.table.LinkDto;
import org.springframework.stereotype.Component;

@Component
public class LinkMapper {
    public LinkDto center(Center center) {
        LinkDto linkDto = new LinkDto();
        linkDto.setId(center.getId());
        linkDto.setName(center.getName());
        return linkDto;
    }

    public LinkDto event(Event event) {
        LinkDto linkDto = new LinkDto();
        linkDto.setId(event.getId());
        linkDto.setName(event.getName());
        return linkDto;
    }
}
