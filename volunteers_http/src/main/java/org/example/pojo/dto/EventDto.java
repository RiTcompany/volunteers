package org.example.pojo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EventDto {
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;
    private String teamLeader;
}
