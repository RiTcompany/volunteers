package org.example.builders;

import org.example.enums.EPageNavigation;
import org.example.pojo.dto.ButtonDto;
import org.example.utils.MessageUtil;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageableInlineKeyboardBuilder extends InlineKeyboardBuilder {
    private final Map<Integer, List<List<InlineKeyboardButton>>> pageMap = new HashMap<>();
    private Integer page = 0;

    protected PageableInlineKeyboardBuilder(String chatId) {
        super(chatId);
    }

    public static PageableInlineKeyboardBuilder create(String chatId) {
        return new PageableInlineKeyboardBuilder(chatId);
    }

    @Override
    public SendMessage build() {
        this.pageMap.clear();
        final int[] counter = {0};
        final int[] currPage = {0};
        pageMap.put(currPage[0], new ArrayList<>());
        this.buttons.values().forEach((button) -> {
            pageMap.get(currPage[0]).add(button);
            counter[0]++;
            if (counter[0] == 10) {
                currPage[0]++;
                pageMap.put(currPage[0], new ArrayList<>());
                counter[0] = 0;
            }

        });

        if (pageMap.keySet().size() != 1) {
            for (Integer key : pageMap.keySet()) {
                pageMap.get(key).add(Arrays.asList(
                        (new ButtonDto(EPageNavigation.PREV.getDescription(), EPageNavigation.PREV.name(), 0))
                                .toKeyboardButton(),
                        (new ButtonDto(EPageNavigation.NEXT.getDescription(), EPageNavigation.NEXT.name(), 0))
                                .toKeyboardButton()
                ));
            }
        }

        inlineKeyboardMarkup.setKeyboard(pageMap.get(page));
        return MessageUtil.completeSendMessage(messageText, chatId, inlineKeyboardMarkup);
    }

    @Override
    public InlineKeyboardMarkup getInlineKeyboardMarkup() {
        build();
//        inlineKeyboardMarkup.setKeyboard(pageMap.get(page));
        return inlineKeyboardMarkup;
    }

    public void incrementPage() {
        page++;
        if (page == pageMap.keySet().size()) {
            page = 0;
        }
    }

    public void decrementPage() {
        page--;
        if (page < 0) {
            page = pageMap.keySet().size() - 1;
        }
    }
}
