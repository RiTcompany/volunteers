package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.builders.MessageBuilder;
import org.example.entities.ChildDocument;
import org.example.enums.ECheckDocumentStatus;
import org.example.mappers.ChildDocumentMapper;
import org.example.repositories.ChildDocumentRepository;
import org.example.services.ChildDocumentService;
import org.example.utils.MessageUtil;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Service
@RequiredArgsConstructor
public class ChildDocumentServiceImpl implements ChildDocumentService {
    private final ChildDocumentRepository childDocumentRepository;
    private final ChildDocumentMapper childDocumentMapper;
    private static final String ACCEPT_MESSAGE_TEXT = "Модератор принял ваш документ";
    private static final String FAIL_MESSAGE_TEXT = "Модератор не принял ваш документ. Комментарий:\n";

    @Override
    public void create(long chaId, String path) {
        ChildDocument childDocument = childDocumentMapper.childDocument(chaId, path);
        childDocumentRepository.saveAndFlush(childDocument);
    }

    @Override
    public ChildDocument getToCheckDocument() {
        return childDocumentRepository.findFirstByStatus(ECheckDocumentStatus.NEW).orElse(null);
    }

    @Override
    public void acceptDocument(long moderatorId, AbsSender sender) {
        ChildDocument childDocument = childDocumentRepository
                .findFirstByStatusAndModeratorId(ECheckDocumentStatus.CHECKING, moderatorId)
                .orElse(null);
        completeCheckingDocument(childDocument, ECheckDocumentStatus.ACCEPTED, null);
        sendFailMessage(sender, ACCEPT_MESSAGE_TEXT, childDocument);
    }

    @Override
    public void failDocument(long moderatorId, String message, AbsSender sender) {
        ChildDocument childDocument = childDocumentRepository
                .findFirstByStatusAndModeratorId(ECheckDocumentStatus.CHECKING, moderatorId)
                .orElse(null);
        completeCheckingDocument(childDocument, ECheckDocumentStatus.FAILED, message);
        sendFailMessage(sender, FAIL_MESSAGE_TEXT.concat(message), childDocument);
    }

    @Override
    public void saveAndFlush(ChildDocument childDocument) {
        childDocumentRepository.saveAndFlush(childDocument);
    }

    private void completeCheckingDocument(ChildDocument childDocument, ECheckDocumentStatus status, String message) {
        if (childDocument != null) {
            childDocument.setStatus(status);
            childDocument.setMessage(message);
            saveAndFlush(childDocument);
        }
    }

    private void sendFailMessage(AbsSender sender, String message, ChildDocument childDocument) {
        if (childDocument != null) {
            MessageUtil.sendMessage(
                    MessageBuilder.create()
                            .setText(message)
                            .sendMessage(childDocument.getChatId()),
                    sender
            );
        }
    }
}
