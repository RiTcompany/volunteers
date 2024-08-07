package org.example.repositories;

import org.example.entities.BotMessage;
import org.example.enums.EBotMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BotMessageRepository extends JpaRepository<BotMessage, Long> {
    Optional<BotMessage> findByWriterIdAndStatus(long writerId, EBotMessage status);
}
