package org.example.repositories;

import org.example.entities.BotMessageButton;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BotMessageButtonRepository extends JpaRepository<BotMessageButton, Long> {
    Optional<BotMessageButton> findByBotMessageId(long botMessageId);
    List<BotMessageButton> findAllByBotMessageId(long botMessageId);
}
