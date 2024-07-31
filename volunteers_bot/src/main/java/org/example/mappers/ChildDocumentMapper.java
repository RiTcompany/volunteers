package org.example.mappers;

import org.example.entities.ChildDocument;
import org.example.enums.ECheckDocumentStatus;
import org.springframework.stereotype.Component;

@Component
public class ChildDocumentMapper {
    public ChildDocument childDocument(long chatId, String path) {
        ChildDocument childDocument = new ChildDocument();
        childDocument.setChatId(chatId);
        childDocument.setPath(path);
        childDocument.setStatus(ECheckDocumentStatus.NEW);
        return childDocument;
    }
}
