package org.example.steps.impl.moderator;

import lombok.RequiredArgsConstructor;
import org.example.builders.MessageBuilder;
import org.example.dto.MessageDto;
import org.example.dto.ResultDto;
import org.example.entities.BotUser;
import org.example.entities.ChatHash;
import org.example.entities.DocumentToCheck;
import org.example.enums.EDocument;
import org.example.enums.ERole;
import org.example.enums.EYesNo;
import org.example.exceptions.AbstractException;
import org.example.exceptions.EntityNotFoundException;
import org.example.mappers.KeyboardMapper;
import org.example.services.BotUserService;
import org.example.services.DocumentService;
import org.example.steps.ChoiceStep;
import org.example.utils.ButtonUtil;
import org.example.utils.MessageUtil;
import org.example.utils.ValidUtil;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.File;

@RequiredArgsConstructor
public abstract class DocumentCheckChoiceStep extends ChoiceStep {
    private final DocumentService documentService;
    private final KeyboardMapper keyboardMapper;
    private final BotUserService botUserService;

    protected abstract String getPrepareMessageText();

    protected abstract String getAcceptMessageText();

    protected abstract EDocument getDocumentType();

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) throws AbstractException {
        BotUser botUser = botUserService.getByChatIdAndRole(chatHash.getId(), ERole.ROLE_MODERATOR);
        DocumentToCheck documentToCheck = documentService.getCheckingDocument(
                botUser.getId(), getDocumentType()
        );

        Message message = MessageUtil.sendDocument(
                MessageBuilder.create()
                        .setFile(new File(documentToCheck.getPath()))
                        .setText(getPrepareMessageText())
                        .setInlineKeyBoard(keyboardMapper.keyboardDto(chatHash, ButtonUtil.yesNoButtonList()))
                        .sendDocument(chatHash.getId()),
                sender
        );

        int messageId = message != null ? message.getMessageId() : -1;
        chatHash.setPrevBotMessageId(messageId);
    }

    @Override
    protected ResultDto isValidData(MessageDto messageDto) {
        return ValidUtil.isValidYesNoChoice(messageDto, EXCEPTION_MESSAGE_TEXT);
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) throws EntityNotFoundException {
        EYesNo answer = EYesNo.valueOf(data);
        if (EYesNo.NO.equals(answer)) {
            return 0;
        }

        long moderatorId = botUserService.getByChatIdAndRole(chatHash.getId(), ERole.ROLE_MODERATOR).getId();
        DocumentToCheck documentToCheck = documentService.saveAcceptResponse(
                moderatorId, getDocumentType()
        );
        sendMessageToVolunteer(sender, documentToCheck.getChatId());
        return 1;

    }

    private void sendMessageToVolunteer(AbsSender sender, long chatId) {
        MessageUtil.sendMessage(
                MessageBuilder.create()
                        .setText(getAcceptMessageText())
                        .sendMessage(chatId),
                sender
        );
    }
}
