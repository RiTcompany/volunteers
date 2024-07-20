package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.pojo.entities.Volonteer;
import org.example.repositories.VolonteerRepository;
import org.example.services.VolonteerService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VolonteerServiceImpl implements VolonteerService {
    private final VolonteerRepository volonteerRepository;

    @Override
    public Volonteer getVolonteerByChatId(long chatId) {
        return volonteerRepository.findByChatId(chatId)
                .orElseGet(() -> {
                    log.error("No TG link for volonteer chat ID={}", chatId);
                    return new Volonteer(chatId);
                }); // TODO : потом нужно как-то всё равно добавлять ссылку на тг
//        но это вариант, когда волонтер не создался в бд (хотя должен создаваться в команде RegisterCommand)
//        следовательно, он попал в диалог не через команду, а это не должно быть возможно
//        поэтому предлагаю поставить тут просто какую-то метку, но ничего с этим не делать
    }

    @Override
    public void saveAndFlushVolonteer(Volonteer volonteer) {
        volonteerRepository.saveAndFlush(volonteer);
    }
}
