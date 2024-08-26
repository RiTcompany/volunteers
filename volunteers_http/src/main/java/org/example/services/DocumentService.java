package org.example.services;

import org.example.pojo.dto.DocumentDto;
import org.example.pojo.filters.DocumentFilter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DocumentService {
    List<DocumentDto> getDocumentList(DocumentFilter filter);
}
