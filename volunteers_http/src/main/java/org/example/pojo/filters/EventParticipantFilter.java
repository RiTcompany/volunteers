package org.example.pojo.filters;

import lombok.Getter;
import lombok.Setter;
import org.example.enums.EParticipant;

import java.util.List;

@Getter
@Setter
public class EventParticipantFilter {
    private Integer minAge;
    private Integer maxAge;
    private boolean OrderByDateAsc;
    private boolean OrderByDateDesc;
    private List<EParticipant> functionalList;
    private Boolean testing;
    private Double minRank;
    private boolean OrderByRankAsc;
    private boolean OrderByRankDesc;
    private Boolean hasClothes;
}
