package org.example.utils;

import org.example.entities.ChatHash;
import org.example.enums.EMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class LogUtil {
    public static String getConversationLog(Update update, EMessage eMessage, ChatHash chatHash) {
        return chatHash == null
                ? "Chat ID - %d\tUsername - %s\tType - %s\tInput - %s\tConversation - null".formatted(
                UpdateUtil.getChatId(update),
                UpdateUtil.getUserName(update),
                eMessage,
                UpdateUtil.getUserInputText(update)
        )
                : "Chat ID - %d\tUsername - %s\tType - %s\tInput - %s\tConversation - %s\tStep - %s".formatted(
                UpdateUtil.getChatId(update),
                UpdateUtil.getUserName(update),
                eMessage,
                UpdateUtil.getUserInputText(update),
                chatHash.getEConversation(),
                chatHash.getEConversationStep()
        );
    }

    public static String getAddToGroupChatLog(long groupChatId, long addedByUserId) {
        return "Chat ID - %d\tAdviser ID - %d\tStatus - Add to group chat".formatted(groupChatId, addedByUserId);
    }

    public static String getKickFromGroupChatLog(long groupChatId) {
        return "Chat ID - %d\tStatus - Del from group chat".formatted(groupChatId);
    }

    public static String getCommandLog(Update update) {
        return "Chat ID - %d\tUsername - %s\tCommand - %s".formatted(
                UpdateUtil.getChatId(update),
                UpdateUtil.getUserName(update),
                UpdateUtil.getUserInputText(update)
        );
    }

    public static String getExceptionLog(Update update, String exceptionMessage) {
        return "Chat ID - %d\tUsername - %s\tInput - %s\tException - %s".formatted(
                UpdateUtil.getChatId(update),
                UpdateUtil.getUserName(update),
                UpdateUtil.getUserInputText(update),
                exceptionMessage
        );
    }
}
