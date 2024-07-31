package org.example.repositories;

import org.example.entities.Moderator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ModeratorRepository extends JpaRepository<Moderator, Long> {
    boolean existsByChatId(long chatId);

    Optional<Moderator> findByChatId(long chatId);
}
