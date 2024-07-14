package org.example.steps;

import lombok.RequiredArgsConstructor;
import org.example.pojo.entities.Volonteer;
import org.example.repositories.VolonteerRepository;

@RequiredArgsConstructor
public abstract class VolonteerConversationStep extends ConversationStep {
    private final VolonteerRepository volonteerRepository;

    protected Volonteer findVolonteerByChatId(long chatId) {
        return volonteerRepository.findByChatId(chatId).orElseGet(() -> new Volonteer(chatId));
    }

    protected void saveAndFlushVolonteer(Volonteer volonteer) {
        volonteerRepository.saveAndFlush(volonteer);
    }
}
