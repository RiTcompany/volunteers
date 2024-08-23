package org.example.services;

import org.example.entities.DocumentToCheck;
import org.example.enums.EDocument;
import org.example.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface DocumentService {
    void create(long chaId, String path, EDocument eDocument);

    DocumentToCheck getToCheck(EDocument eDocument);

    DocumentToCheck getCheckingDocument(long moderatorId, EDocument eDocument) throws EntityNotFoundException;

    DocumentToCheck saveAcceptResponse(long moderatorId, EDocument eDocument) throws EntityNotFoundException;

    DocumentToCheck saveFailResponse(long moderatorId, String message, EDocument eDocument) throws EntityNotFoundException;

    void setModerator(DocumentToCheck documentToCheck, long botUserId);

    boolean mayChangeDocument(long chatId, EDocument eDocument);
}
