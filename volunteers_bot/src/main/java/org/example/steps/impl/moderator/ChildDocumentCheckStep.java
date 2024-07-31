package org.example.steps.impl.moderator;

import lombok.RequiredArgsConstructor;
import org.example.builders.MessageBuilder;
import org.example.dto.KeyboardDto;
import org.example.entities.ChildDocument;
import org.example.enums.EAgreement;
import org.example.enums.ECheckDocumentStatus;
import org.example.exceptions.EntityNotFoundException;
import org.example.dto.ResultDto;
import org.example.entities.ChatHash;
import org.example.mappers.KeyboardMapper;
import org.example.repositories.ModeratorRepository;
import org.example.services.ChildDocumentService;
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
    private final ModeratorRepository moderatorRepository;
    private static final String PREPARE_MESSAGE_TEXT = """
            Проверьте документ.
            1) Если он удовлетворяет всем критериям - нажмите ОК
            2) Иначе напишите, что сделано неверно, чтобы регистрирующийся смог увидеть ваш комментарий""";
    private static final String NO_DOCS_PREPARE_MESSAGE_TEXT = "Сейчас нет документов для проверки";

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) {
        ChildDocument document = childDocumentService.getToCheckDocument();
        if (document != null) {
            updateDocument(chatHash.getId(), document);
        }

        int messageId = document != null
                ? sendDocument(document, chatHash, sender)
                : sendMessage(chatHash, sender);

        chatHash.setPrevBotMessageId(messageId);
    }

    @Override
    protected String getPrepareMessageText() {
        return PREPARE_MESSAGE_TEXT;
    }

    @Override
    protected ResultDto isValidData(String data) {
        return new ResultDto(true);
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) throws EntityNotFoundException {
        moderatorRepository.findByChatId(chatHash.getId()).ifPresent(moderator -> {
                    if (EAgreement.YES.toString().equals(data)) {
                        childDocumentService.acceptDocument(moderator.getId(), sender);
                    } else {
                        childDocumentService.failDocument(moderator.getId(), data, sender);
                    }
                }
        );
        return 0;
    }

    private void updateDocument(long chatId, ChildDocument document) {
        moderatorRepository.findByChatId(chatId).ifPresent(moderator ->
                document.setModeratorId(moderator.getId())
        );

        document.setStatus(ECheckDocumentStatus.CHECKING);
        childDocumentService.saveAndFlush(document);
    }

    private int sendDocument(ChildDocument document, ChatHash chatHash, AbsSender sender) {
        KeyboardDto keyboardDto = keyboardMapper.keyboardDto(chatHash, ButtonUtil.yesButtonList());
        return MessageUtil.sendDocument(
                MessageBuilder.create()
                        .setFile(new File(document.getPath()))
                        .setText(PREPARE_MESSAGE_TEXT)
                        .setInlineKeyBoard(keyboardDto)
                        .sendDocument(chatHash.getId()),
                sender
        );
    }

    private int sendMessage(ChatHash chatHash, AbsSender sender) {
        KeyboardDto keyboardDto = keyboardMapper.keyboardDto(chatHash, ButtonUtil.yesButtonList());
        return MessageUtil.sendMessage(
                MessageBuilder.create()
                        .setText(NO_DOCS_PREPARE_MESSAGE_TEXT)
                        .setInlineKeyBoard(keyboardDto)
                        .sendMessage(chatHash.getId()),
                sender
        );
    }
}
