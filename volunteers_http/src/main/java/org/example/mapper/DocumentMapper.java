package org.example.mapper;

import org.example.entities.Document;
import org.example.pojo.dto.DocumentDto;
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
}
