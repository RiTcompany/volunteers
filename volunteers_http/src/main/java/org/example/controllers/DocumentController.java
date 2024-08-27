package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.pojo.dto.CenterDto;
import org.example.pojo.dto.DocumentDto;
import org.example.pojo.filters.DocumentFilter;
import org.example.services.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentService documentService;

    @GetMapping("/center_document/{centerId}")
    public ResponseEntity<List<DocumentDto>> getCenterDocumentList(
            @PathVariable Long centerId, @RequestBody DocumentFilter filter
    ) {
        return ResponseEntity.ok(documentService.getCenterDocumentList(centerId, filter));
    }

    @GetMapping("/district_team_document/{districtTeamId}")
    public ResponseEntity<List<DocumentDto>> getDistrictTeamDocumentList(
            @PathVariable Long districtTeamId, @RequestBody DocumentFilter filter
    ) {
        return ResponseEntity.ok(documentService.getDistrictTeamDocumentList(districtTeamId, filter));
    }
}
