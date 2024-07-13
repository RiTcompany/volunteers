package org.example.builders;

import lombok.Getter;
import lombok.Setter;
import org.example.pojo.dto.ButtonDto;
import org.example.utils.MessageUtil;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InlineKeyboardBuilder {
    protected final InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
    protected final List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
    protected final Map<Integer, List<InlineKeyboardButton>> buttons = new HashMap<>();
    @Getter @Setter
    protected String messageText = "Wait...";
    protected final String chatId;

    protected InlineKeyboardBuilder(String chatId) {
        this.chatId = chatId;
    }

    public static InlineKeyboardBuilder create(String chatId) {
        return new InlineKeyboardBuilder(chatId);
    }

    public void addButton(ButtonDto button) {
        int row = button.getRow();
        if (!this.buttons.containsKey(row)) {
            this.buttons.put(row, new ArrayList<>());
        }

        this.buttons.get(row).add(button.toKeyboardButton());
    }

    public InlineKeyboardMarkup getInlineKeyboardMarkup() {
        build();
        return inlineKeyboardMarkup;
    }

    public SendMessage build() {
        this.rowList.clear();
        this.rowList.addAll(this.buttons.values());
        inlineKeyboardMarkup.setKeyboard(rowList);

        return MessageUtil.completeSendMessage(messageText, chatId, inlineKeyboardMarkup);
    }
}
