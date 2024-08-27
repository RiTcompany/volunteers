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
    private boolean OrderByDateAsc;
    private boolean OrderByDateDesc;
}
