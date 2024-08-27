package org.example.mapper;

import org.example.entities.Center;
import org.example.entities.Event;
import org.example.entities.Headquarters;
import org.example.pojo.dto.LinkDto;
import org.springframework.stereotype.Component;

@Component
public class LinkMapper {
    public LinkDto headquarters(Headquarters headquarters) {
        LinkDto linkDto = new LinkDto();
        linkDto.setName(headquarters.getName());
        linkDto.setLink(headquarters.getLink());
        return linkDto;
    }

    public LinkDto center(Center center) {
        LinkDto linkDto = new LinkDto();
        linkDto.setName(center.getName());
        linkDto.setLink(center.getLink());
        return linkDto;
    }

    public LinkDto event(Event event) {
        LinkDto linkDto = new LinkDto();
        linkDto.setName(event.getName());
        linkDto.setLink(event.getLink());
        return linkDto;
    }
}
