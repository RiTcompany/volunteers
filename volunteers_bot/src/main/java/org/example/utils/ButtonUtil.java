package org.example.utils;

import org.example.enums.EAgreement;
import org.example.enums.EPageMove;
import org.example.dto.ButtonDto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ButtonUtil {
    public static final String OTHER_CHOICE = "Другое";
    private static List<InlineKeyboardButton> pageMoveButtonList;
    private static List<ButtonDto> yesList;
    private static List<ButtonDto> clearList;

    public static void addOtherChoice(List<ButtonDto> buttonDtoList) {
        buttonDtoList.add(new ButtonDto(OTHER_CHOICE, OTHER_CHOICE, buttonDtoList.size()));
    }

    public static List<InlineKeyboardButton> pageMoveButtonList() {
        if (pageMoveButtonList == null) {
            pageMoveButtonList = getPageMoveButtonList();
        }

        return pageMoveButtonList;
    }

    public static List<ButtonDto> okButtonList() {
        if (yesList == null) {
            yesList = getYesButtonDtoList();
        }

        return yesList;
    }

    public static List<ButtonDto> clearButtonList() {
        if (yesList == null) {
            yesList = getClearButtonDtoList();
        }

        return yesList;
    }

    private static List<InlineKeyboardButton> getPageMoveButtonList() {
        return Arrays.asList(
                (new ButtonDto(EPageMove.PREV.name(), EPageMove.PREV.getDescription())).toKeyboardButton(),
                (new ButtonDto(EPageMove.NEXT.name(), EPageMove.NEXT.getDescription())).toKeyboardButton()
        );
    }

    private static List<ButtonDto> getYesButtonDtoList() {
        List<ButtonDto> buttonDtoList = new ArrayList<>();
        buttonDtoList.add(new ButtonDto(EAgreement.OK.toString(), EAgreement.OK.getAgreementStr(), 0));
        return buttonDtoList;
    }

    private static List<ButtonDto> getClearButtonDtoList() {
        List<ButtonDto> buttonDtoList = new ArrayList<>();
        buttonDtoList.add(new ButtonDto(EAgreement.CLEARLY.toString(), EAgreement.CLEARLY.getAgreementStr(), 0));
        return buttonDtoList;
    }
}
