package org.example.pojo.dto.update;

import lombok.Getter;
import lombok.Setter;
import org.example.enums.EColor;

@Getter
@Setter
public class ParticipantUpdateDto {
    private EColor color;
    private Boolean hasInterview;
    private String level;
    private Long centerId;
}
