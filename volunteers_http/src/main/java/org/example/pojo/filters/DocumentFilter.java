package org.example.pojo.filters;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class DocumentFilter {
    private Date startDate;
    private Date endDate;
    private Boolean approvalControl;
    private Long centerId;
    private Long districtTeamId;
    private boolean OrderByDateAsc;
    private boolean OrderByDateDesc;
}
