package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.entities.Document;
import org.example.mapper.DocumentMapper;
import org.example.pojo.dto.DocumentDto;
import org.example.pojo.filters.DocumentFilter;
import org.example.repositories.DocumentRepository;
import org.example.services.DocumentService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;

    @Override
    public List<DocumentDto> getDocumentList(DocumentFilter filter) {
        Stream<Document> stream = documentRepository.findAll().stream();

        stream =
                filterByEndDate(
                        filterByStartDate(
                                filterByApprovalControl(
                                        filterByDistrictTeamId(
                                                filterByCenterId(stream, filter), filter
                                        ), filter
                                ), filter
                        ), filter
                );

        stream =
                sortByDateDesc(
                        sortByDateAsc(stream, filter), filter
                );

        return stream.map(documentMapper::documentDto).toList();
    }

    private Stream<Document> filterByCenterId(Stream<Document> stream, DocumentFilter filter) {
        if (filter.getCenterId() != null) {
            return stream.filter(document ->
                    filter.getCenterId().equals(document.getCenterId())
            );
        }

        return stream;
    }

    private Stream<Document> filterByDistrictTeamId(Stream<Document> stream, DocumentFilter filter) {
        if (filter.getDistrictTeamId() != null) {
            stream = stream.filter(document ->
                    filter.getDistrictTeamId().equals(document.getDistrictTeamId())
            );
        }

        return stream;
    }

    private Stream<Document> filterByApprovalControl(Stream<Document> stream, DocumentFilter filter) {
        if (filter.getApprovalControl() != null) {
            stream = stream.filter(document ->
                    filter.getApprovalControl().equals(document.isApprovalControl())
            );
        }

        return stream;
    }

    private Stream<Document> filterByStartDate(Stream<Document> stream, DocumentFilter filter) {
        if (filter.getStartDate() != null) {
            stream = stream.filter(document ->
                    !filter.getStartDate().after(document.getCreateDate())
            );
        }

        return stream;
    }

    private Stream<Document> filterByEndDate(Stream<Document> stream, DocumentFilter filter) {
        stream = stream.filter(document ->
                !filter.getEndDate().before(document.getCreateDate())
        );

        return stream;
    }

    private Stream<Document> sortByDateAsc(Stream<Document> stream, DocumentFilter filter) {
        if (filter.isOrderByDateAsc()) {
            stream = stream.sorted(Comparator.comparing(Document::getCreateDate));
        }

        return stream;
    }

    private Stream<Document> sortByDateDesc(Stream<Document> stream, DocumentFilter filter) {
        if (filter.isOrderByDateDesc()) {
            stream = stream.sorted(Comparator.comparing(Document::getCreateDate).reversed());
        }

        return stream;
    }
}
