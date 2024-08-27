package org.example.mapper;

import org.example.entities.Document;
import org.example.pojo.dto.table.DocumentDto;
import org.example.pojo.dto.update.DocumentUpdateDto;
import org.springframework.stereotype.Component;

@Component
public class DocumentMapper {
    public DocumentDto documentDto(Document document) {
        DocumentDto documentDto = new DocumentDto();
        documentDto.setId(document.getId());
        documentDto.setName(document.getName());
        documentDto.setSender(document.getSender());
        documentDto.setRecipient(document.getRecipient());
        documentDto.setCreateDate(document.getCreateDate());
        documentDto.setApprovalControl(document.isApprovalControl());
        return documentDto;
    }

    public Document document(Document document, DocumentUpdateDto updateDto) {
        if (updateDto.getApprovalControl() != null) {
            document.setApprovalControl(updateDto.getApprovalControl());
        }

        return document;
    }

    public Document document(DocumentDto documentDto) {
        Document document = new Document();
        document.setName(documentDto.getName());
        document.setSender(documentDto.getSender());
        document.setRecipient(documentDto.getRecipient());
        document.setCreateDate(documentDto.getCreateDate());
        document.setApprovalControl(documentDto.isApprovalControl());
        return document;
    }
}
