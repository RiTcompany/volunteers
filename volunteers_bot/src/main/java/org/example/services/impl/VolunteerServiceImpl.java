package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.EntityNotFoundException;
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
    public Volunteer getByChatId(long chatId) throws EntityNotFoundException {
        return volunteerRepository.findByChatId(chatId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Не существует волонтёра с чатом ID = ".concat(String.valueOf(chatId)),
                        "Что-то пошло не так, пожалуйста обратитесь в поддержку"
                ));
    }

    @Override
    public void saveAndFlush(Volunteer volunteer) {
        volunteerRepository.saveAndFlush(volunteer);
    }
}
