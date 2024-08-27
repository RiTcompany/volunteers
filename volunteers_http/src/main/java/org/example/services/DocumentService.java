package org.example.services;

import org.example.pojo.dto.DocumentDto;
import org.example.pojo.filters.DocumentFilter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DocumentService {
    List<DocumentDto> getCenterDocumentList(long centerId, DocumentFilter filter);

    List<DocumentDto> getDistrictTeamDocumentList(long districtTeamId, DocumentFilter filter);
}
