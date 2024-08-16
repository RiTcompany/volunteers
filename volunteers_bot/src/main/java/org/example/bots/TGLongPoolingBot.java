package org.example.bots;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.AbstractException;
import org.example.services.UpdateHandleService;
import org.example.utils.LogUtil;
import org.example.utils.MessageUtil;
import org.example.utils.UpdateUtil;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
public final class TGLongPoolingBot extends TelegramLongPollingBot {
    @Getter
    private final String botUsername;
    private final UpdateHandleService updateHandleService;

    public TGLongPoolingBot(
            String botUsername, String token, UpdateHandleService updateHandleService
    ) {
        super(token);
        this.botUsername = botUsername;
        this.updateHandleService = updateHandleService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasCallbackQuery() && UpdateUtil.isPrivateChat(update)) {
                updateHandleService.handleCallbackRequest(update, this);
            } else if (update.hasMessage() && UpdateUtil.isPrivateChat(update)) {
                updateHandleService.handleMessageRequest(update, this);
            } else if (update.hasMyChatMember()) {
                updateHandleService.handleMyChatMember(update);
            }
        } catch (AbstractException e) {
            log.error(LogUtil.getExceptionLog(update, e.getMessage()));
            MessageUtil.sendMessageText(UpdateUtil.getChatId(update), e.getUserMessage(), this);
        }
    }
}
