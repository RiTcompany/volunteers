package org.example.steps;

import lombok.AccessLevel;
import lombok.Setter;
import org.example.enums.EConversationStep;
import org.example.enums.PageMoveEnum;
import org.example.mappers.KeyboardMapper;
import org.example.pojo.dto.ButtonDto;
import org.example.pojo.dto.KeyboardDto;
import org.example.pojo.dto.MessageDto;
import org.example.pojo.entities.ChatHash;
import org.example.repositories.VolonteerRepository;
import org.example.utils.KeyboardUtil;
import org.example.utils.MessageUtil;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;

public abstract class ChoiceStep extends VolonteerConversationStep {
    protected static final String EXCEPTION_MESSAGE_TEXT = "Выберите один из выше предложенных вариантов";
    private final KeyboardMapper keyboardMapper;
    @Setter(value = AccessLevel.PROTECTED)
    private List<ButtonDto> buttonDtoList;

    public ChoiceStep(
            VolonteerRepository volonteerRepository,
            String prepareMessageText,
            KeyboardMapper keyboardMapper
    ) {
        super(volonteerRepository, prepareMessageText);
        this.keyboardMapper = keyboardMapper;
    }

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) {
        setButtonList(chatHash);
        KeyboardDto keyboardDto = keyboardMapper.keyboardDto(
                chatHash, buttonDtoList, PREPARE_MESSAGE_TEXT
        );
        SendMessage sendMessage = KeyboardUtil.sendMessageWithKeyboard(keyboardDto);
        int messageId = MessageUtil.sendMessage(sendMessage, sender);
        chatHash.setPrevBotMessageId(messageId);
    }

    @Override
    public EConversationStep execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
        setButtonList(chatHash);
        if (isMovePageAction(chatHash, messageDto, sender)) {
            return chatHash.getEConversationStep();
        }

        return null;
    }

    protected abstract void setButtonList(ChatHash chatHash);

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
