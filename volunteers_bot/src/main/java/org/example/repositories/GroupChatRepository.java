package org.example.repositories;

import jakarta.transaction.Transactional;
import org.example.entities.GroupChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupChatRepository extends JpaRepository<GroupChat, Long> {
    @Modifying
    @Transactional
    void deleteByChatId(long chatId);
}
