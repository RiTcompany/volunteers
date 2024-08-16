package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entities.GroupChat;
import org.example.mappers.GroupChatMapper;
import org.example.repositories.GroupChatRepository;
import org.example.services.BotUserService;
import org.example.services.GroupChatService;
import org.example.utils.LogUtil;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupChatServiceImpl implements GroupChatService {
    private final GroupChatRepository groupChatRepository;
    private final GroupChatMapper groupChatMapper;
    private final BotUserService botUserService;

    @Override
    public void save(ChatMemberUpdated chatMemberUpdated) {
        long groupChatId = chatMemberUpdated.getChat().getId();
        long addedByUserId = chatMemberUpdated.getFrom().getId();

        if (botUserService.existsByTgId(addedByUserId)) {
            GroupChat groupChat = groupChatMapper.groupChat(groupChatId, addedByUserId);
            groupChatRepository.saveAndFlush(groupChat);
        }

        log.info(LogUtil.getAddToGroupChatLog(groupChatId, addedByUserId));
    }

    @Override
    public void delete(ChatMemberUpdated chatMemberUpdated) {
        long groupChatId = chatMemberUpdated.getChat().getId();
        groupChatRepository.deleteByChatId(groupChatId);

        log.info(LogUtil.getKickFromGroupChatLog(groupChatId));
    }

    @Override
    public List<GroupChat> findAll() {
        return groupChatRepository.findAll();
    }

    @Override
    public boolean isBotNewChatMember(ChatMemberUpdated chatMemberUpdated) {
        boolean notExistsBefore = "left".equals(chatMemberUpdated.getOldChatMember().getStatus());
        boolean existsNow = "member".equals(chatMemberUpdated.getNewChatMember().getStatus());
        return notExistsBefore && existsNow;
    }

    @Override
    public boolean isBotKickedChatMember(ChatMemberUpdated chatMemberUpdated) {
        String newStatus = chatMemberUpdated.getNewChatMember().getStatus();
        boolean notExists = "left".equals(newStatus);
        boolean kicked = "kicked".equals(newStatus);
        return notExists || kicked;
    }
}
