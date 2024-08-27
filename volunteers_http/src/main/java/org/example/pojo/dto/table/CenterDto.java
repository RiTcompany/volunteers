package org.example.pojo.dto.table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CenterDto {
    private Long id;
    private String name;
    private int participantCount;
    private String location;
    private String contact;
}
