package org.example.repositories;

import org.example.entities.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParentRepository extends JpaRepository<Parent, Long> {
    Optional<Parent> findByChatId(Long chatId);

    boolean existsByChatId(long chatId);
}
