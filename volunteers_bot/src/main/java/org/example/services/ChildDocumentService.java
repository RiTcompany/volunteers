package org.example.services;

import org.example.entities.ChildDocument;
import org.example.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Service
public interface ChildDocumentService {
    void create(long chaId, String path);

    ChildDocument getToCheck();

    ChildDocument getCheckingDocument(long moderatorId) throws EntityNotFoundException;

    ChildDocument accept(long moderatorId, AbsSender sender) throws EntityNotFoundException;

    ChildDocument fail(long moderatorId, String message, AbsSender sender) throws EntityNotFoundException;

    void saveAndFlush(ChildDocument childDocument);
}
