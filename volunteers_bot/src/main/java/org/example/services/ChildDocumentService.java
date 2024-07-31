package org.example.services;

import org.example.entities.ChildDocument;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Service
public interface ChildDocumentService {
    void create(long chaId, String path);

    ChildDocument getToCheckDocument();

    void acceptDocument(long moderatorId, AbsSender sender);

    void failDocument(long moderatorId, String message, AbsSender sender);

    void saveAndFlush(ChildDocument childDocument);
}
