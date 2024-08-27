package org.example.pojo.filters;

import lombok.Getter;
import lombok.Setter;
import org.example.enums.EColor;

import java.util.List;

@Getter
@Setter
public class DistrictParticipantFilter {
    private Integer minAge;
    private Integer maxAge;
    private boolean OrderByDateAsc;
    private boolean OrderByDateDesc;
    private List<EColor> colorList;
    private List<Long> eventIdList;
}