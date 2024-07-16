package org.example.builders;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.pojo.dto.ButtonDto;
import org.example.pojo.entities.ChatHash;
import org.example.utils.MessageUtil;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InlineKeyboardBuilder {
    protected final List<List<InlineKeyboardButton>> rowButtonList = new ArrayList<>();
    @Getter @Setter
    protected String messageText = "Wait...";

    public static InlineKeyboardBuilder create() {
        return new InlineKeyboardBuilder();
    }

    public void addButtonList(List<ButtonDto> buttonDtoList) {
        for (ButtonDto buttonDto : buttonDtoList) {
            addButton(buttonDto);
        }
    }

    public SendMessage buildSendMessage(long chatId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowButtonList);
        return MessageUtil.completeSendMessage(
                messageText, String.valueOf(chatId), inlineKeyboardMarkup
        );
    }

    public EditMessageReplyMarkup buildEditMessageReplyMarkup(long chatId, int messageId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowButtonList);
        return MessageUtil.completeEditMessageReplyMarkup(
                String.valueOf(chatId), messageId, inlineKeyboardMarkup
        );
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
