package org.example.steps.impl.moderator;

import lombok.RequiredArgsConstructor;
import org.example.builders.MessageBuilder;
import org.example.dto.MessageDto;
import org.example.dto.ResultDto;
import org.example.entities.ChatHash;
import org.example.entities.ChildDocument;
import org.example.entities.Moderator;
import org.example.enums.EAgreement;
import org.example.enums.EMessage;
import org.example.exceptions.EntityNotFoundException;
import org.example.mappers.KeyboardMapper;
import org.example.services.ChildDocumentService;
import org.example.services.ModeratorService;
import org.example.steps.ChoiceStep;
import org.example.utils.ButtonUtil;
import org.example.utils.MessageUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.File;

@Component
@RequiredArgsConstructor
public class ChildDocumentCheckStep extends ChoiceStep {
    private final ChildDocumentService childDocumentService;
    private final KeyboardMapper keyboardMapper;
    private final ModeratorService moderatorService;
    private static final String PREPARE_MESSAGE_TEXT = """
            Проверьте документ.
            1) Если он удовлетворяет всем критериям - нажмите ОК
            2) Иначе напишите, что сделано неверно, чтобы регистрирующийся смог увидеть ваш комментарий""";
    private static final String ACCEPT_MESSAGE_TEXT = "Модератор принял ваш документ";
    private static final String FAIL_MESSAGE_TEXT = "Модератор не принял ваш документ. Комментарий:\n";


    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) throws EntityNotFoundException {
        Moderator moderator = moderatorService.getModeratorByChatId(chatHash.getId());
        ChildDocument document = childDocumentService.getCheckingDocument(moderator.getId());

        int messageId = MessageUtil.sendDocument(
                MessageBuilder.create()
                        .setFile(new File(document.getPath()))
                        .setText(PREPARE_MESSAGE_TEXT)
                        .setInlineKeyBoard(keyboardMapper.keyboardDto(chatHash, ButtonUtil.okButtonList()))
                        .sendDocument(chatHash.getId()),
                sender
        );
        chatHash.setPrevBotMessageId(messageId);
    }

    @Override
    protected ResultDto isValidData(MessageDto messageDto) {
        EMessage eMessage = messageDto.getEMessage();
        if (!EMessage.CALLBACK.equals(eMessage) && !EMessage.TEXT.equals(eMessage)) {
            return new ResultDto(false, "Нужно нажать кнопку, если документ корректный, либо ввести текстовый комментарий, если нет");
        }

        return new ResultDto(true);
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) throws EntityNotFoundException {
        long moderatorId = moderatorService.getModeratorByChatId(chatHash.getId()).getId();
        if (EAgreement.OK.toString().equals(data)) {
            ChildDocument childDocument = childDocumentService.accept(moderatorId, sender);
            sendMessageToVolunteer(sender, ACCEPT_MESSAGE_TEXT, childDocument.getChatId());
        } else {
            ChildDocument childDocument = childDocumentService.fail(moderatorId, data, sender);
            sendMessageToVolunteer(sender, FAIL_MESSAGE_TEXT.concat(data), childDocument.getChatId());
        }

        return 0;
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
