package org.example.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Getter @Setter @ToString
@NoArgsConstructor
public class ButtonDto {
    private String callback;
    private String text;
    private int row;

    public ButtonDto(String callback, String text) {
        this.callback = callback;
        this.text = text;
    }

    public ButtonDto(String callback, String text, int row) {
        this.callback = callback;
        this.text = text;
        this.row = row;
    }

    public InlineKeyboardButton toKeyboardButton() {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callback);

        return button;
    }
}
