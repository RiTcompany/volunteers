package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.pojo.entities.Volunteer;
import org.example.repositories.VolunteerRepository;
import org.example.services.VolunteerService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VolunteerServiceImpl implements VolunteerService {
    private final VolunteerRepository volunteerRepository;

    @Override
    public Volunteer getByChatId(long chatId) {
        return volunteerRepository.findByChatId(chatId)
                .orElseGet(() -> {
                    log.error("No TG link for volunteer chat ID={}", chatId);
                    return new Volunteer(chatId);
                }); // TODO : потом нужно как-то всё равно добавлять ссылку на тг
//        но это вариант, когда волонтер не создался в бд (хотя должен создаваться в команде RegisterCommand)
//        следовательно, он попал в диалог не через команду, а это не должно быть возможно
//        поэтому предлагаю поставить тут просто какую-то метку, но ничего с этим не делать
    }

    @Override
    public void saveAndFlush(Volunteer volunteer) {
        volunteerRepository.saveAndFlush(volunteer);
    }
}
