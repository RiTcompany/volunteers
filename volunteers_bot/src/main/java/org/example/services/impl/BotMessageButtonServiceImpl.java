package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.entities.BotMessageButton;
import org.example.mappers.BotMessageButtonMapper;
import org.example.repositories.BotMessageButtonRepository;
import org.example.services.BotMessageButtonService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BotMessageButtonServiceImpl implements BotMessageButtonService {
    private final BotMessageButtonRepository botMessageButtonRepository;
    private final BotMessageButtonMapper botMessageButtonMapper;

    @Override
    public BotMessageButton create(long botMessageId, String buttonName, String buttonLink) {
        BotMessageButton botMessageButton = botMessageButtonMapper.botMessageButton(
                botMessageId, buttonName, buttonLink
        );
        return botMessageButtonRepository.saveAndFlush(botMessageButton);
    }

    @Override
    public List<BotMessageButton> getListByMessageId(long botMessageId) {
        return botMessageButtonRepository.findAllByBotMessageId(botMessageId);
    }

    @Override
    public void deleteButtons(long botMessageId) {
        botMessageButtonRepository.deleteAllByBotMessageId(botMessageId);
    }
}
