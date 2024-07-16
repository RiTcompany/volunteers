package org.example.steps;

import lombok.extern.slf4j.Slf4j;
import org.example.pojo.entities.ChatHash;
import org.example.pojo.entities.Volonteer;
import org.example.repositories.VolonteerRepository;

@Slf4j
public abstract class VolonteerConversationStep extends ConversationStep {
    private final VolonteerRepository volonteerRepository;

    public VolonteerConversationStep(
            VolonteerRepository volonteerRepository, String prepareMessageText
    ) {
        super(prepareMessageText);
        this.volonteerRepository = volonteerRepository;
    }

    protected Volonteer getVolonteer(ChatHash chatHash) {
        return volonteerRepository.findByChatId(chatHash.getId())
                .orElseGet(() -> {
                    log.error("No TG link for volonteer chat ID={}", chatHash.getId());
                    return new Volonteer(chatHash.getId());
                }); // TODO : потом нужно как-то всё равно добавлять ссылку на тг
    }

    protected void saveAndFlushVolonteer(Volonteer volonteer) {
        volonteerRepository.saveAndFlush(volonteer);
    }
}
