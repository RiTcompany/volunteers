package org.example.utils;

import org.example.builders.MessageBuilder;
import org.example.dto.KeyboardDto;
import org.example.dto.MessageDto;
import org.example.entities.ChatHash;
import org.example.enums.EPageMove;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class StepUtil {
    public static void sendPrepareMessageOnlyText(ChatHash chatHash, String message, AbsSender sender) {
        SendMessage sendMessage = MessageBuilder.create()
                .setText(message)
                .sendMessage(chatHash.getId());
        int messageId = MessageUtil.sendMessage(sendMessage, sender);
        chatHash.setPrevBotMessageId(messageId);
    }

    public static void sendPrepareMessageWithInlineKeyBoard(
            ChatHash chatHash, String message, KeyboardDto keyboardDto, AbsSender sender
    ) {
        int messageId = MessageUtil.sendMessage(
                MessageBuilder.create()
                        .setText(message)
                        .setInlineKeyBoard(keyboardDto)
                        .sendMessage(chatHash.getId()),
                sender
        );
        chatHash.setPrevBotMessageId(messageId);
    }

    public static void sendPrepareMessageWithPageableKeyBoard(
            ChatHash chatHash, String message, KeyboardDto keyboardDto, AbsSender sender
    ) {
        int messageId = MessageUtil.sendMessage(
                MessageBuilder.create()
                        .setText(message)
                        .setPageableKeyBoard(keyboardDto)
                        .sendMessage(chatHash.getId()),
                sender
        );
        chatHash.setPrevBotMessageId(messageId);
    }

    public static boolean isMovePageAction(ChatHash chatHash, MessageDto messageDto, KeyboardDto keyboardDto, AbsSender sender) {
        try {
            EPageMove ePageMove = EPageMove.valueOf(messageDto.getData());
            changePage(ePageMove, keyboardDto, chatHash, sender);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static void changePage(EPageMove ePageMove, KeyboardDto keyboardDto, ChatHash chatHash, AbsSender sender) {
        int newPageNumber = KeyboardUtil.movePage(ePageMove, keyboardDto, sender);
        chatHash.setPrevBotMessagePageNumber(newPageNumber);
    }
}
