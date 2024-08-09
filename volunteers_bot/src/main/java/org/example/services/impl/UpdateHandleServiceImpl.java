package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.enums.EMessage;
import org.example.exceptions.CommandException;
import org.example.services.ConversationService;
import org.example.services.UpdateHandleService;
import org.example.utils.UpdateUtil;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.CommandRegistry;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Service
@RequiredArgsConstructor
public class UpdateHandleServiceImpl implements UpdateHandleService {
    private final CommandRegistry commandRegistry;
    private final ConversationService conversationService;

    @Override
    public void handleMyChatMember(Update update) {
        ChatMemberUpdated chatMemberUpdated = update.getMyChatMember();
        if (isBotNewChatMember(chatMemberUpdated)) {
            long groupChatId = chatMemberUpdated.getChat().getId();
            String chatTitle = chatMemberUpdated.getChat().getTitle();

            String addedByUserName = chatMemberUpdated.getFrom().getUserName();
            long addedByUserId = chatMemberUpdated.getFrom().getId();

            System.out.printf("Бот был добавлен в группу: %s (ID: %d)%n", chatTitle, groupChatId);
            System.out.printf("Бот был добавлен пользователем: %s (ID: %d)%n", addedByUserName, addedByUserId);
        }
    }

    @Override
    public void handleCallbackRequest(Update update, AbsSender sender) throws CommandException {
        conversationService.executeConversationStep(update, EMessage.CALLBACK, sender);
    }

    @Override
    public void handleMessageRequest(Update update, AbsSender sender) throws CommandException {
        Message message = update.getMessage();
        if (message.isCommand()) {
            executeCommand(update, sender);
        } else if (message.hasDocument()) {
            conversationService.executeConversationStep(update, EMessage.DOCUMENT, sender);
        } else if (message.hasPhoto()) {
            conversationService.executeConversationStep(update, EMessage.PHOTO, sender);
        } else if (message.hasText()) {
            conversationService.executeConversationStep(update, EMessage.TEXT, sender);
        }

    }

    private void executeCommand(Update update, AbsSender sender) throws CommandException {
        Message message = update.getMessage();
        conversationService.executeConversationStep(update, EMessage.COMMAND, sender);
        if (!commandRegistry.executeCommand(sender, message)) {
            throw new CommandException(
                    "Вызов несуществующей команды %s"
                            .formatted(UpdateUtil.getUserInputText(update)),
                    "Такой команды не существует"
            );
        }
    }

    private boolean isBotNewChatMember(ChatMemberUpdated chatMemberUpdated) {
        boolean notExistsBefore = chatMemberUpdated.getOldChatMember().getStatus().equals("left");
        boolean existsNow = chatMemberUpdated.getNewChatMember().getStatus().equals("member");
        return notExistsBefore && existsNow;
    }
}
