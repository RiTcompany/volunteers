package org.example.steps;

import lombok.AccessLevel;
import lombok.Setter;
import org.example.builders.MessageBuilder;
import org.example.enums.EPageMove;
import org.example.exceptions.EntityNotFoundException;
import org.example.mappers.KeyboardMapper;
import org.example.dto.ButtonDto;
import org.example.dto.KeyboardDto;
import org.example.dto.MessageDto;
import org.example.dto.ResultDto;
import org.example.entities.ChatHash;
import org.example.utils.KeyboardUtil;
import org.example.utils.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;

@Component
public abstract class ChoiceStep extends ConversationStep {
    protected static final String EXCEPTION_MESSAGE_TEXT = "Выберите один из выше предложенных вариантов";
    @Autowired
    private KeyboardMapper keyboardMapper;
    @Setter(value = AccessLevel.PROTECTED)
    private List<ButtonDto> buttonDtoList;

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) throws EntityNotFoundException {
        KeyboardDto keyboardDto = keyboardMapper.keyboardDto(
                chatHash, buttonDtoList, getPrepareMessageText()
        );
        SendMessage sendMessage = MessageBuilder.create()
                .setText(getPrepareMessageText())
                .setPageableKeyBoard(keyboardDto)
                .sendMessage(chatHash.getId());
        int messageId = MessageUtil.sendMessage(sendMessage, sender);
        chatHash.setPrevBotMessageId(messageId);
    }

    @Override
    public int execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) throws EntityNotFoundException {
        if (isMovePageAction(chatHash, messageDto, sender)) {
            return -1;
        }

        String data = messageDto.getData();
        ResultDto result = isValidData(data);
        if (!result.isDone()) {
            return handleIllegalUserAction(messageDto, sender, result.getMessage());
        }

        deleteKeyboard(chatHash, sender);
        return finishStep(chatHash, sender, data);
    }

    protected abstract ResultDto isValidData(String data);

    @Override
    protected void sendFinishMessage(ChatHash chatHash, AbsSender sender, String text) {
        super.sendFinishMessage(chatHash, sender, text);
    }

    private void deleteKeyboard(ChatHash chatHash, AbsSender sender) {
        long chatId = chatHash.getId();
        int keyBoardMessageId = chatHash.getPrevBotMessageId();
        KeyboardUtil.cleanKeyboard(chatId, keyBoardMessageId, sender);
        chatHash.setDefaultPrevBotMessagePageNumber();
    }

    private boolean isMovePageAction(ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
        try {
            EPageMove ePageMove = EPageMove.valueOf(messageDto.getData());
            changePage(ePageMove, chatHash, sender);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private void changePage(EPageMove ePageMove, ChatHash chatHash, AbsSender sender) {
        KeyboardDto keyboardDto = keyboardMapper.keyboardDto(chatHash, buttonDtoList);
        int newPageNumber = KeyboardUtil.movePage(ePageMove, keyboardDto, sender);
        chatHash.setPrevBotMessagePageNumber(newPageNumber);
    }
}
