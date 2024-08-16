package org.example.mappers;

import org.example.entities.GroupChat;
import org.springframework.stereotype.Component;

@Component
public class GroupChatMapper {
    public GroupChat groupChat(long chatId, long adviserTgId) {
        GroupChat groupChat = new GroupChat();
        groupChat.setChatId(chatId);
        groupChat.setAdviserTgId(adviserTgId);
        return groupChat;
    }
}
