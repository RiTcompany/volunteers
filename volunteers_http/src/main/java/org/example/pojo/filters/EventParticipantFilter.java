package org.example.pojo.filters;

import org.example.enums.EParticipant;

import java.util.List;

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
