package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.pojo.dto.table.DocumentDto;
import org.example.pojo.dto.update.DocumentUpdateDto;
import org.example.pojo.filters.DocumentFilter;
import org.example.services.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentService documentService;

    @PostMapping("/center_document/{centerId}")
    public ResponseEntity<List<DocumentDto>> getCenterDocumentList(
            @PathVariable Long centerId, @RequestBody DocumentFilter filter
    ) {
        return ResponseEntity.ok(documentService.getCenterDocumentList(centerId, filter));
    }

    @PostMapping("/district_team_document/{districtTeamId}")
    public ResponseEntity<List<DocumentDto>> getDistrictTeamDocumentList(
            @PathVariable Long districtTeamId, @RequestBody DocumentFilter filter
    ) {
        return ResponseEntity.ok(documentService.getDistrictTeamDocumentList(districtTeamId, filter));
    }

    @PatchMapping("/document/{id}")
    public ResponseEntity<Long> updateDocument(
            @PathVariable Long id, @RequestBody DocumentUpdateDto updateDto
    ) {
        return ResponseEntity.ok(documentService.updateDocument(id, updateDto));
    }

    @PostMapping("/document")
    public ResponseEntity<Long> addDocument(@RequestBody DocumentDto documentDto) {
        return ResponseEntity.ok(documentService.addDocument(documentDto));
    }
}
