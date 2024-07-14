package org.example.pojo.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Getter
@RequiredArgsConstructor
public class ButtonDto {
    private final String callback;
    private final String text;
    private final int row;

    public InlineKeyboardButton toKeyboardButton() {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callback);

        return button;
    }
}
