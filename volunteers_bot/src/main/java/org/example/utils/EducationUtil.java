package org.example.utils;

import org.example.enums.EEducationInstitution;
import org.example.enums.EEducationStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EducationUtil {
//    TODO : точно придётся вынести из статики
    public static final String OTHER_ANSWER = "Другое";
    public static final List<String> SCHOOL_LIST = List.of("Школа №1", "Школа №2", "Школа №3", "Школа №4", "Школа №5", "Школа №5", "Школа №6", "Школа №7", "Школа №8", "Школа №9", "Школа №10", "Школа №11", OTHER_ANSWER);
    public static final List<String> UNIVERSITY_LIST = List.of("Институт №1", "Институт №2", "Институт №3", "Институт №4", "Институт №5", "Институт №6", "Институт №7", "Институт №8", "Институт №9", "Институт №10", "Институт №11", OTHER_ANSWER);
    public static final List<String> SECONDARY_PROFESIONAL_LIST = List.of("ПТУ №1", "ПТУ №2", "ПТУ №3", "ПТУ №4", "ПТУ №5", "ПТУ №6", "ПТУ №7", "ПТУ №8", "ПТУ №9", "ПТУ №10", "ПТУ №11", OTHER_ANSWER);
    private static final Map<EEducationStatus, EEducationInstitution> eduStatusToInstitution = new HashMap<>() {{
        put(EEducationStatus.STUDY_SCHOOL, EEducationInstitution.SCHOOL);
        put(EEducationStatus.STUDY_UNIVERSITY, EEducationInstitution.UNIVERSITY);
        put(EEducationStatus.FINISHED_UNIVERSITY, EEducationInstitution.UNIVERSITY);
        put(EEducationStatus.STUDY_SECONDARY_PROFESSIONAL, EEducationInstitution.SECONDARY_PROFESSIONAL);
        put(EEducationStatus.FINISHED_SECONDARY_PROFESSIONAL, EEducationInstitution.SECONDARY_PROFESSIONAL);
    }};

    public static List<String> getEducationInstitutionList(EEducationStatus eEducationStatus) {
        return switch (eEducationStatus) {
            case STUDY_SCHOOL -> SCHOOL_LIST;
            case STUDY_UNIVERSITY, FINISHED_UNIVERSITY -> UNIVERSITY_LIST;
            case STUDY_SECONDARY_PROFESSIONAL, FINISHED_SECONDARY_PROFESSIONAL -> SECONDARY_PROFESIONAL_LIST;
        };
    }

    public static EEducationInstitution getEEducationInstitution(EEducationStatus eEducationStatus) {
        return eduStatusToInstitution.get(eEducationStatus);
    }
}
