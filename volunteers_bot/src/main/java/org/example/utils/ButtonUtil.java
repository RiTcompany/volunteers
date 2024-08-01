package org.example.utils;

import org.example.dto.ButtonDto;
import org.example.enums.EPageMove;
import org.example.enums.EYesNo;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ButtonUtil {
    public static final String OTHER_CHOICE = "Другое";
    public static final String OK_ANSWER = "OK";
    private static List<InlineKeyboardButton> pageMoveButtonList;
    private static List<ButtonDto> okList;
    private static List<ButtonDto> yesNoList;

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
        if (okList == null) {
            okList = getOkButtonDtoList();
        }

        return okList;
    }

    public static List<ButtonDto> yesNoButtonList() {
        if (yesNoList == null) {
            yesNoList = getYesNoButtonDtoList();
        }

        return yesNoList;
    }

    private static List<InlineKeyboardButton> getPageMoveButtonList() {
        return Arrays.asList(
                (new ButtonDto(EPageMove.PREV.name(), EPageMove.PREV.getString())).toKeyboardButton(),
                (new ButtonDto(EPageMove.NEXT.name(), EPageMove.NEXT.getString())).toKeyboardButton()
        );
    }

    private static List<ButtonDto> getOkButtonDtoList() {
        List<ButtonDto> buttonDtoList = new ArrayList<>();
        buttonDtoList.add(new ButtonDto(OK_ANSWER, OK_ANSWER, 0));
        return buttonDtoList;
    }

    private static List<ButtonDto> getYesNoButtonDtoList() {
        EYesNo[] eYesNoArray = EYesNo.values();
        List<ButtonDto> buttonDtoList = new ArrayList<>();
        for (int i = 0; i < eYesNoArray.length; i++) {
            buttonDtoList.add(new ButtonDto(eYesNoArray[i].toString(), eYesNoArray[i].getString(), i));
        }

        return buttonDtoList;
    }
}
