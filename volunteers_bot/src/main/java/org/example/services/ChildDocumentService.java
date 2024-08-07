package org.example.services;

import org.example.entities.ChildDocument;
import org.example.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface ChildDocumentService {
    void create(long chaId, String path);

    ChildDocument getToCheck();

    ChildDocument getCheckingDocument(long moderatorId) throws EntityNotFoundException;

    ChildDocument accept(long moderatorId) throws EntityNotFoundException;

    ChildDocument fail(long moderatorId, String message) throws EntityNotFoundException;

    void update(ChildDocument childDocument, long botUserId);
}
