package org.example.repositories;

import org.example.entities.ChildDocument;
import org.example.enums.ECheckDocumentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChildDocumentRepository extends JpaRepository<ChildDocument, Long> {
    Optional<ChildDocument> findFirstByStatus(ECheckDocumentStatus status);

    Optional<ChildDocument> findFirstByStatusAndModeratorId(ECheckDocumentStatus status, Long moderatorId);
}
