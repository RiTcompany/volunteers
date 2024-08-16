package org.example.utils;

import org.example.builders.MessageBuilder;
import org.example.dto.KeyboardDto;
import org.example.dto.MessageDto;
import org.example.entities.ChatHash;
import org.example.enums.EPageMove;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class StepUtil {
    public static void sendPrepareMessageOnlyText(ChatHash chatHash, String messageText, AbsSender sender) {
        SendMessage sendMessage = MessageBuilder.create().setText(messageText)
                .sendMessage(chatHash.getId());

        Message message = MessageUtil.sendMessage(sendMessage, sender);
        int messageId = message != null ? message.getMessageId() : -1;
        chatHash.setPrevBotMessageId(messageId);
    }

    public static void sendPrepareMessageWithInlineKeyBoard(
            ChatHash chatHash, String messageText, KeyboardDto keyboardDto, AbsSender sender
    ) {
        Message message = MessageUtil.sendMessage(
                MessageBuilder.create().setText(messageText).setInlineKeyBoard(keyboardDto)
                        .sendMessage(chatHash.getId()),
                sender
        );

        int messageId = message != null ? message.getMessageId() : -1;
        chatHash.setPrevBotMessageId(messageId);
    }

    public static void sendPrepareMessageWithPageableKeyBoard(
            ChatHash chatHash, String messageText, KeyboardDto keyboardDto, AbsSender sender
    ) {
        Message message = MessageUtil.sendMessage(
                MessageBuilder.create().setText(messageText).setPageableKeyBoard(keyboardDto)
                        .sendMessage(chatHash.getId()),
                sender
        );

        int messageId = message != null ? message.getMessageId() : -1;
        chatHash.setPrevBotMessageId(messageId);
    }

    public static boolean isMovePageAction(ChatHash chatHash, MessageDto messageDto, KeyboardDto keyboardDto, AbsSender sender) {
        try {
            EPageMove ePageMove = EPageMove.valueOf(messageDto.getData());
            changePage(ePageMove, keyboardDto, chatHash, sender);
            return true;
        } catch (IllegalArgumentException ignored) {
        }

        return false;
    }

    private static void changePage(EPageMove ePageMove, KeyboardDto keyboardDto, ChatHash chatHash, AbsSender sender) {
        int newPageNumber = KeyboardUtil.movePage(ePageMove, keyboardDto, sender);
        chatHash.setPrevBotMessagePageNumber(newPageNumber);
    }
}
