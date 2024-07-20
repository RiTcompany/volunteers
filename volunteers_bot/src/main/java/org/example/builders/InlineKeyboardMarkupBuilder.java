package org.example.builders;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.pojo.dto.ButtonDto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InlineKeyboardMarkupBuilder {
    protected final List<List<InlineKeyboardButton>> rowButtonList = new ArrayList<>();

    public static InlineKeyboardMarkupBuilder create() {
        return new InlineKeyboardMarkupBuilder();
    }

    public InlineKeyboardMarkup build() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowButtonList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkupBuilder setButtonList(List<ButtonDto> buttonDtoList) {
        buttonDtoList.forEach(this::addButton);
        return this;
    }

    protected void addButton(ButtonDto button) {
        int row = button.getRow();
        addButton(row, button);
    }

    protected void addButton(int row, ButtonDto buttonDto) {
        while (rowButtonList.size() <= row) {
            rowButtonList.add(new ArrayList<>());
        }

        rowButtonList.get(row).add(buttonDto.toKeyboardButton());
    }
}
