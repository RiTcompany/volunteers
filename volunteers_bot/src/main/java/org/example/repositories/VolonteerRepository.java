package org.example.repositories;

import org.example.pojo.entities.Volonteer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VolonteerRepository extends JpaRepository<Volonteer, Long> {
    Optional<Volonteer> findByChatId(Long chatId);
}
