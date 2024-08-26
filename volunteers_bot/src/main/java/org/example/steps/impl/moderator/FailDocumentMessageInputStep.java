package org.example.steps.impl.moderator;

import lombok.RequiredArgsConstructor;
import org.example.builders.MessageBuilder;
import org.example.dto.MessageDto;
import org.example.dto.ResultDto;
import org.example.entities.ChatHash;
import org.example.entities.DocumentToCheck;
import org.example.enums.EDocument;
import org.example.enums.ERole;
import org.example.exceptions.EntityNotFoundException;
import org.example.services.BotUserService;
import org.example.services.DocumentService;
import org.example.steps.InputStep;
import org.example.utils.MessageUtil;
import org.example.utils.StepUtil;
import org.example.utils.ValidUtil;
import org.telegram.telegrambots.meta.bots.AbsSender;

@RequiredArgsConstructor
public abstract class FailDocumentMessageInputStep extends InputStep {
    private final DocumentService documentService;
    private final BotUserService botUserService;

    protected abstract String getPrepareMessageText();

    protected abstract String getFailMessageText();

    protected abstract EDocument getDocumentType();

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) throws EntityNotFoundException {
        StepUtil.sendPrepareMessageOnlyText(chatHash, getPrepareMessageText(), sender);
    }

    @Override
    public int execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) throws EntityNotFoundException {
        return super.execute(chatHash, messageDto, sender);
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) throws EntityNotFoundException {
        long moderatorId = botUserService.getByChatIdAndRole(chatHash.getId(), ERole.ROLE_MODERATOR).getId();
        DocumentToCheck documentToCheck = documentService.saveFailResponse(
                moderatorId, data, getDocumentType()
        );
        sendMessageToVolunteer(sender, getFailMessageText().concat(data), documentToCheck.getChatId());
        return 0;
    }

    @Override
    protected ResultDto isValidData(String data) {
        return ValidUtil.isValidShortString(data);
    }

    private void sendMessageToVolunteer(AbsSender sender, String message, long chatId) {
        MessageUtil.sendMessage(
                MessageBuilder.create()
                        .setText(message)
                        .sendMessage(chatId),
                sender
        );
    }
}

