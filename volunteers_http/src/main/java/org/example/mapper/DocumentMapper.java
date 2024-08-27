package org.example.mapper;

import lombok.RequiredArgsConstructor;
import org.example.entities.Document;
import org.example.pojo.dto.table.DocumentDto;
import org.example.pojo.dto.update.DocumentUpdateDto;
import org.example.repositories.DocumentRepository;
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
}
