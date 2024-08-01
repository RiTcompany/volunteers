package org.example.utils;

import org.example.enums.EEducationInstitution;
import org.example.enums.EEducationStatus;

import java.util.HashMap;
import java.util.Map;

public class EducationUtil {
    private static final Map<EEducationStatus, EEducationInstitution> educationStatusToInstitution = new HashMap<>() {{
        put(EEducationStatus.STUDY_SCHOOL, EEducationInstitution.SCHOOL);
        put(EEducationStatus.STUDY_UNIVERSITY, EEducationInstitution.UNIVERSITY);
        put(EEducationStatus.FINISHED_UNIVERSITY, EEducationInstitution.UNIVERSITY);
        put(EEducationStatus.STUDY_SECONDARY_PROFESSIONAL, EEducationInstitution.SECONDARY_PROFESSIONAL);
        put(EEducationStatus.FINISHED_SECONDARY_PROFESSIONAL, EEducationInstitution.SECONDARY_PROFESSIONAL);
    }};

    public static EEducationInstitution getInstitutionType(EEducationStatus eEducationStatus) {
        return educationStatusToInstitution.get(eEducationStatus);
    }
}
