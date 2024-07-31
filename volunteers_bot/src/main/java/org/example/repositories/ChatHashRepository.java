package org.example.repositories;

import org.example.entities.ChatHash;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatHashRepository extends CrudRepository<ChatHash, Long> {
}
