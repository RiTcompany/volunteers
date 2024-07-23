package org.example.steps;

import lombok.AccessLevel;
import lombok.Setter;
import org.example.enums.PageMoveEnum;
import org.example.mappers.KeyboardMapper;
import org.example.pojo.dto.ButtonDto;
import org.example.pojo.dto.KeyboardDto;
import org.example.pojo.dto.MessageDto;
import org.example.pojo.entities.ChatHash;
import org.example.utils.KeyboardUtil;
import org.example.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;

@Component
public abstract class ChoiceStep extends ConversationStep {
    protected static final String EXCEPTION_MESSAGE_TEXT = "Выберите один из выше предложенных вариантов";
    @Autowired
    private KeyboardMapper keyboardMapper;
    @Setter(value = AccessLevel.PROTECTED)
    private List<ButtonDto> buttonDtoList;

    protected abstract void setButtonList();

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) {
        setButtonList();
        KeyboardDto keyboardDto = keyboardMapper.keyboardDto(
                chatHash, buttonDtoList, getPREPARE_MESSAGE_TEXT()
        );
        int messageId = MessageUtil.sendMessageReplyMarkup(keyboardDto, sender);
        chatHash.setPrevBotMessageId(messageId);
    }

    @Override
    public int execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
        setButtonList();
        if (isMovePageAction(chatHash, messageDto, sender)) {
            return -1;
        }

        return 0;
    }

    private boolean isMovePageAction(ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
        try {
            PageMoveEnum pageMoveEnum = PageMoveEnum.valueOf(messageDto.getData());
            changePage(pageMoveEnum, chatHash, sender);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private void changePage(PageMoveEnum pageMoveEnum, ChatHash chatHash, AbsSender sender) {
        KeyboardDto keyboardDto = keyboardMapper.keyboardDto(chatHash, buttonDtoList);
        int newPageNumber = KeyboardUtil.movePage(pageMoveEnum, keyboardDto, sender);
        chatHash.setPrevBotMessagePageNumber(newPageNumber);
    }
}
