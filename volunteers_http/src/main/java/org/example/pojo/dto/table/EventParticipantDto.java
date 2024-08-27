package org.example.pojo.dto.table;

import lombok.Getter;
import lombok.Setter;
import org.example.enums.EParticipant;

@Getter
@Setter
public class EventParticipantDto {
    private String id;
    private String fullName;
    private String birthday;
    private String tgLink;
    private EParticipant functional;
    private boolean testing;
    private String comment;
    private double rank;
    private boolean hasClothes;
}
