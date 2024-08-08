package org.example.repositories;

import org.example.entities.DocumentToCheck;
import org.example.enums.ECheckStatus;
import org.example.enums.EDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<DocumentToCheck, Long> {
    Optional<DocumentToCheck> findFirstByStatusAndDocumentType(ECheckStatus status, EDocument eDocument);

    Optional<DocumentToCheck> findFirstByStatusAndModeratorIdAndDocumentType(
            ECheckStatus status, Long moderatorId, EDocument eDocument
    );
}
