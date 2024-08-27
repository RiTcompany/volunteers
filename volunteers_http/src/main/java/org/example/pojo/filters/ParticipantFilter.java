package org.example.pojo.filters;

import lombok.Getter;
import lombok.Setter;
import org.example.enums.EColor;
import org.example.enums.EParticipant;

import java.util.List;

@Getter
@Setter
public class ParticipantFilter {
    private Integer minAge;
    private Integer maxAge;
    private boolean OrderByDateAsc;
    private boolean OrderByDateDesc;
    private Double minRank;
    private boolean OrderByRankAsc;
    private boolean OrderByRankDesc;
    private List<Long> eventIdList;
    private List<EColor> colorList;
    private Boolean hasInterview;
    private List<String> levelList;
    private List<EParticipant> functionalList;
    private Boolean testing;
    private Boolean hasClothes;
    private List<Long> centerIdList;
}
