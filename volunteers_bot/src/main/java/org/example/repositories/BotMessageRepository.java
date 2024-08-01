package org.example.repositories;

import org.example.entities.BotMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BotMessageRepository extends JpaRepository<BotMessage, Long> {
}
