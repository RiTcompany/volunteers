package org.example.pojo.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Getter
@Setter
public class ButtonDto {
    private String callback;
    private String text;
    private int row;

    public InlineKeyboardButton toKeyboardButton() {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callback);

        return button;
    }
}
