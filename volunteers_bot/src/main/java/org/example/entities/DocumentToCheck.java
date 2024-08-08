package org.example.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.example.enums.ECheckStatus;
import org.example.enums.EDocument;

@Entity
@Table(name = "document_to_check")
@Getter
@Setter
public class DocumentToCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String path;

    @Enumerated(EnumType.STRING)
    private ECheckStatus status;

    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Column(name = "moderator_id")
    private Long moderatorId;

    private String message;

    @Column(name = "document_type")
    @Enumerated(EnumType.STRING)
    private EDocument documentType;
}
