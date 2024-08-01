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
import org.example.enums.ECheckDocumentStatus;

@Entity
@Table(name = "child_document")
@Getter
@Setter
public class ChildDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String path;

    @Enumerated(EnumType.STRING)
    private ECheckDocumentStatus status;

    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Column(name = "moderator_id")
    private Long moderatorId;

    private String message;
}
