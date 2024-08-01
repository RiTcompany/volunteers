package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.entities.ChildDocument;
import org.example.enums.ECheckDocumentStatus;
import org.example.exceptions.EntityNotFoundException;
import org.example.mappers.ChildDocumentMapper;
import org.example.repositories.ChildDocumentRepository;
import org.example.services.ChildDocumentService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Service
@RequiredArgsConstructor
public class ChildDocumentServiceImpl implements ChildDocumentService {
    private final ChildDocumentRepository childDocumentRepository;
    private final ChildDocumentMapper childDocumentMapper;

    @Override
    public void create(long chaId, String path) {
        ChildDocument childDocument = childDocumentMapper.childDocument(chaId, path);
        childDocumentRepository.saveAndFlush(childDocument);
    }

    @Override
    public ChildDocument getToCheck() {
        return childDocumentRepository.findFirstByStatus(ECheckDocumentStatus.NEW).orElse(null);
    }

    @Override
    public ChildDocument getCheckingDocument(long moderatorId) throws EntityNotFoundException {
        return childDocumentRepository
                .findFirstByStatusAndModeratorId(ECheckDocumentStatus.CHECKING, moderatorId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Не существует документа с moderatorID = ".concat(String.valueOf(moderatorId)),
                        "Что-то пошло не так, пожалуйста обратитесь в поддержку"
                ));
    }

    @Override
    public ChildDocument accept(long moderatorId, AbsSender sender) throws EntityNotFoundException {
        ChildDocument childDocument = getCheckingDocument(moderatorId);
        childDocument.setStatus(ECheckDocumentStatus.ACCEPTED);
        saveAndFlush(childDocument);
        return childDocument;
    }

    @Override
    public ChildDocument fail(long moderatorId, String message, AbsSender sender) throws EntityNotFoundException {
        ChildDocument childDocument = getCheckingDocument(moderatorId);
        childDocument.setStatus(ECheckDocumentStatus.FAILED);
        childDocument.setMessage(message);
        saveAndFlush(childDocument);
        return childDocument;
    }

    @Override
    public void saveAndFlush(ChildDocument childDocument) {
        childDocumentRepository.saveAndFlush(childDocument);
    }
}
