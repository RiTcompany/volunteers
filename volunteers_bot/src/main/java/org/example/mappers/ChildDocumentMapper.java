package org.example.mappers;

import org.example.entities.DocumentToCheck;
import org.example.enums.ECheckStatus;
import org.example.enums.EDocument;
import org.springframework.stereotype.Component;

@Component
public class ChildDocumentMapper {
    public DocumentToCheck childDocument(long chatId, String path, EDocument eDocument) {
        DocumentToCheck documentToCheck = new DocumentToCheck();
        documentToCheck.setChatId(chatId);
        documentToCheck.setPath(path);
        documentToCheck.setStatus(ECheckStatus.NEW);
        documentToCheck.setDocumentType(eDocument);
        return documentToCheck;
    }
}
