package org.example.utils;

import org.example.enums.ECity;
import org.example.enums.EEducationInstitution;
import org.example.enums.EEducationStatus;
import org.example.enums.EGender;
import org.example.enums.PageMoveEnum;
import org.example.pojo.dto.ButtonDto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ButtonUtil {
    private static List<InlineKeyboardButton> pageMoveButtonList;
    private static final Map<EEducationInstitution, List<ButtonDto>> eduStatusToInstitutionList = new HashMap<>() {{
        put(EEducationInstitution.SCHOOL, null);
        put(EEducationInstitution.UNIVERSITY, null);
        put(EEducationInstitution.SECONDARY_PROFESSIONAL, null);
    }};
    private static List<ButtonDto> cityList;
    private static List<ButtonDto> educationStatusList;
    private static List<ButtonDto> genderList;

    public static List<InlineKeyboardButton> pageMoveButtonList() {
        if (pageMoveButtonList == null) {
            pageMoveButtonList = getPageMoveButtonList();
        }

        return pageMoveButtonList;
    }

    public static List<ButtonDto> educationInstitutionButtonList(EEducationStatus eEducationStatus) {
        EEducationInstitution eEducationInstitution = EducationUtil.getEEducationInstitution(eEducationStatus);
        if (eduStatusToInstitutionList.get(eEducationInstitution) == null) {
            eduStatusToInstitutionList.put(eEducationInstitution, getEducationInstitutionButtonList(eEducationStatus));
        }

        return eduStatusToInstitutionList.get(eEducationInstitution);
    }

    public static List<ButtonDto> cityButtonList() {
        if (cityList == null) {
            cityList = getCityButtonDtoList();
        }

        return cityList;
    }

    public static List<ButtonDto> educationStatusButtonList() {
        if (educationStatusList == null) {
            educationStatusList = getEducationStatusButtonDtoList();
        }

        return educationStatusList;
    }

    public static List<ButtonDto> genderButtonList() {
        if (genderList == null) {
            genderList = getGenderButtonDtoList();
        }

        return genderList;
    }

    private static List<InlineKeyboardButton> getPageMoveButtonList() {
        return Arrays.asList(
                (new ButtonDto(PageMoveEnum.PREV.name(), PageMoveEnum.PREV.getDescription())).toKeyboardButton(),
                (new ButtonDto(PageMoveEnum.NEXT.name(), PageMoveEnum.NEXT.getDescription())).toKeyboardButton()
        );
    }

    private static List<ButtonDto> getEducationInstitutionButtonList(EEducationStatus eEducationStatus) {
        List<String> educationInstitutionList = EducationUtil.getEducationInstitutionList(eEducationStatus);
        List<ButtonDto> buttonDtoList = new ArrayList<>();
        for (int i = 0; i < educationInstitutionList.size(); i++) {
            buttonDtoList.add(new ButtonDto(educationInstitutionList.get(i), educationInstitutionList.get(i), i));
        }

        return buttonDtoList;
    }

    private static List<ButtonDto> getCityButtonDtoList() {
        ECity[] eCityArray = ECity.values();
        List<ButtonDto> buttonDtoList = new ArrayList<>();
        for (int i = 0; i < eCityArray.length; i++) {
            buttonDtoList.add(new ButtonDto(eCityArray[i].toString(), eCityArray[i].getCityStr(), i));
        }

        return buttonDtoList;
    }

    private static List<ButtonDto> getEducationStatusButtonDtoList() {
        EEducationStatus[] eEducationStatusArray = EEducationStatus.values();
        List<ButtonDto> buttonDtoList = new ArrayList<>();
        for (int i = 0; i < eEducationStatusArray.length; i++) {
            buttonDtoList.add(new ButtonDto(
                    eEducationStatusArray[i].toString(),
                    eEducationStatusArray[i].getEEducationStatusStr(),
                    i
            ));
        }

        return buttonDtoList;
    }

    private static List<ButtonDto> getGenderButtonDtoList() {
        EGender[] eGenderArray = EGender.values();
        List<ButtonDto> buttonDtoList = new ArrayList<>();
        for (int i = 0; i < eGenderArray.length; i++) {
            buttonDtoList.add(new ButtonDto(eGenderArray[i].toString(), eGenderArray[i].getGenderStr(), i));
        }

        return buttonDtoList;
    }
}
