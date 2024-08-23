package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.entities.DocumentToCheck;
import org.example.enums.ECheckStatus;
import org.example.enums.EDocument;
import org.example.exceptions.EntityNotFoundException;
import org.example.mappers.ChildDocumentMapper;
import org.example.repositories.DocumentRepository;
import org.example.services.DocumentService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;
    private final ChildDocumentMapper childDocumentMapper;

    @Override
    public void create(long chaId, String path, EDocument eDocument) {
        DocumentToCheck documentToCheck = childDocumentMapper.childDocument(chaId, path, eDocument);
        documentRepository.saveAndFlush(documentToCheck);
    }

    @Override
    public DocumentToCheck getToCheck(EDocument eDocument) {
        return documentRepository.findFirstByStatusAndDocumentType(ECheckStatus.NEW, eDocument)
                .orElse(null);
    }

    @Override
    public DocumentToCheck getCheckingDocument(long moderatorId, EDocument eDocument) throws EntityNotFoundException {
        return documentRepository.findFirstByStatusAndModeratorIdAndDocumentType(
                        ECheckStatus.CHECKING, moderatorId, eDocument
                )
                .orElseThrow(() -> new EntityNotFoundException(
                        "Не существует документа (%s) с moderatorID = %d со статусом %s"
                                .formatted(eDocument, moderatorId, ECheckStatus.CHECKING)
                ));
    }

    @Override
    public DocumentToCheck saveAcceptResponse(long moderatorId, EDocument eDocument) throws EntityNotFoundException {
        DocumentToCheck documentToCheck = getCheckingDocument(moderatorId, eDocument);
        documentToCheck.setStatus(ECheckStatus.ACCEPTED);
        documentRepository.saveAndFlush(documentToCheck);
        return documentToCheck;
    }

    @Override
    public DocumentToCheck saveFailResponse(long moderatorId, String message, EDocument eDocument) throws EntityNotFoundException {
        DocumentToCheck documentToCheck = getCheckingDocument(moderatorId, eDocument);
        documentToCheck.setStatus(ECheckStatus.FAILED);
        documentToCheck.setMessage(message);
        documentRepository.saveAndFlush(documentToCheck);
        return documentToCheck;
    }

    @Override
    public void setModerator(DocumentToCheck documentToCheck, long botUserId) {
        documentToCheck.setModeratorId(botUserId);
        documentToCheck.setStatus(ECheckStatus.CHECKING);
        documentRepository.saveAndFlush(documentToCheck);
    }

    @Override
    public boolean mayChangeDocument(long chatId, EDocument eDocument) {
        return documentRepository.isNotExistByChatIdAndDocumentTypeAndNotFinishedStatus(chatId, eDocument);
    }
}
