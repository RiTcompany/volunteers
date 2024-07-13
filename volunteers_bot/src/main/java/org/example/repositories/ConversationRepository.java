package org.example.repositories;

import org.example.pojo.entities.ChatHash;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationRepository extends CrudRepository<ChatHash, Long> {
}
