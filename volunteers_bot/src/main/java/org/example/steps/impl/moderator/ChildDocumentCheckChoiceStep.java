package org.example.steps.impl.moderator;

import lombok.RequiredArgsConstructor;
import org.example.builders.MessageBuilder;
import org.example.dto.MessageDto;
import org.example.dto.ResultDto;
import org.example.entities.BotUser;
import org.example.entities.ChatHash;
import org.example.entities.ChildDocument;
import org.example.enums.ERole;
import org.example.enums.EYesNo;
import org.example.exceptions.EntityNotFoundException;
import org.example.mappers.KeyboardMapper;
import org.example.services.ChildDocumentService;
import org.example.services.UserService;
import org.example.steps.ChoiceStep;
import org.example.utils.ButtonUtil;
import org.example.utils.MessageUtil;
import org.example.utils.ValidUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.File;

@Component
@RequiredArgsConstructor
public class ChildDocumentCheckChoiceStep extends ChoiceStep {
    private final ChildDocumentService childDocumentService;
    private final KeyboardMapper keyboardMapper;
    private final UserService userService;
    private static final String PREPARE_MESSAGE_TEXT = "Проверьте документ. Если он удовлетворяет всем критериям - нажмите ДА, иначе - НЕТ";
    private static final String ACCEPT_MESSAGE_TEXT = "Модератор принял ваш документ";


    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) throws EntityNotFoundException {
        BotUser botUser = userService.getByChatIdAndRole(chatHash.getId(), ERole.ROLE_MODERATOR);
        ChildDocument document = childDocumentService.getCheckingDocument(botUser.getId());

        int messageId = MessageUtil.sendDocument(
                MessageBuilder.create()
                        .setFile(new File(document.getPath()))
                        .setText(PREPARE_MESSAGE_TEXT)
                        .setInlineKeyBoard(keyboardMapper.keyboardDto(chatHash, ButtonUtil.yesNoButtonList()))
                        .sendDocument(chatHash.getId()),
                sender
        );
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

        long moderatorId = userService.getByChatIdAndRole(chatHash.getId(), ERole.ROLE_MODERATOR).getId();
        ChildDocument childDocument = childDocumentService.accept(moderatorId);
        sendMessageToVolunteer(sender, childDocument.getChatId());
        return 1;

    }

    private void sendMessageToVolunteer(AbsSender sender, long chatId) {
        MessageUtil.sendMessage(
                MessageBuilder.create()
                        .setText(ChildDocumentCheckChoiceStep.ACCEPT_MESSAGE_TEXT)
                        .sendMessage(chatId),
                sender
        );
    }
}
