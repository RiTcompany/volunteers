package org.example.steps.impl.moderator;

import lombok.RequiredArgsConstructor;
import org.example.builders.MessageBuilder;
import org.example.dto.MessageDto;
import org.example.dto.ResultDto;
import org.example.entities.ChatHash;
import org.example.entities.ChildDocument;
import org.example.enums.ERole;
import org.example.exceptions.EntityNotFoundException;
import org.example.services.ChildDocumentService;
import org.example.services.UserService;
import org.example.steps.InputStep;
import org.example.utils.MessageUtil;
import org.example.utils.StepUtil;
import org.example.utils.ValidUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@RequiredArgsConstructor
public class FailChildDocumentMessageStep extends InputStep {
    private final ChildDocumentService childDocumentService;
    private final UserService userService;
    private static final String PREPARE_MESSAGE_TEXT = "Укажите <b>комментарий</b> почему вы отклонили данный документ. данное сообщение будет отправлено кандидату";
    private static final String FAIL_MESSAGE_TEXT = "Модератор не принял ваш документ. Комментарий:\n";

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) throws EntityNotFoundException {
        StepUtil.sendPrepareMessage(chatHash, PREPARE_MESSAGE_TEXT, sender);
    }

    @Override
    public int execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) throws EntityNotFoundException {
        return super.execute(chatHash, messageDto, sender);
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) throws EntityNotFoundException {
        long moderatorId = userService.getByChatIdAndRole(chatHash.getId(), ERole.ROLE_MODERATOR).getId();
        ChildDocument childDocument = childDocumentService.fail(moderatorId, data);
        sendMessageToVolunteer(sender, FAIL_MESSAGE_TEXT.concat(data), childDocument.getChatId());
        return 0;
    }

    @Override
    protected ResultDto isValidData(String data) {
        if (ValidUtil.isLongDescriptionText(data)) {
            String exceptionMessage = ValidUtil.getLongMessageExceptionText(ValidUtil.MAX_DESCRIPTION_TEXT_LENGTH);
            return new ResultDto(false, exceptionMessage);
        }

        return new ResultDto(true);
    }

    @Override
    protected void saveData(long chatId, String data) throws EntityNotFoundException {
    } // TODO : да, не нравится

    private void sendMessageToVolunteer(AbsSender sender, String message, long chatId) {
        MessageUtil.sendMessage(
                MessageBuilder.create()
                        .setText(message)
                        .sendMessage(chatId),
                sender
        );
    }
}
