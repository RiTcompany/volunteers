package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entities.Volunteer;
import org.example.exceptions.EntityNotFoundException;
import org.example.mappers.VolunteerMapper;
import org.example.repositories.VolunteerRepository;
import org.example.services.VolunteerService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class VolunteerServiceImpl implements VolunteerService {
    private final VolunteerRepository volunteerRepository;
    private final VolunteerMapper volunteerMapper;

    @Override
    public Volunteer getByChatId(long chatId) throws EntityNotFoundException {
        return volunteerRepository.findByChatId(chatId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Не существует волонтёра с чатом ID = %d".formatted(chatId)
                ));
    }

    @Override
    public void saveAndFlush(Volunteer volunteer) {
        volunteerRepository.saveAndFlush(volunteer);
    }

    public void create(long chatId, String tgUserName) {
        if (!volunteerRepository.existsByChatId(chatId)) {
            volunteerRepository.saveAndFlush(volunteerMapper.volunteer(chatId, tgUserName));
        }
    }
}
