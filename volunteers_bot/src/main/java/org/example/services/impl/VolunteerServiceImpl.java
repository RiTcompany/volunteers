package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entities.Volunteer;
import org.example.enums.EClothingSize;
import org.example.enums.EEducationStatus;
import org.example.enums.EGender;
import org.example.exceptions.EntityNotFoundException;
import org.example.mappers.VolunteerMapper;
import org.example.repositories.VolunteerRepository;
import org.example.services.VolunteerService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VolunteerServiceImpl implements VolunteerService {
    private final VolunteerRepository volunteerRepository;
    private final VolunteerMapper volunteerMapper;
    private static final String TG_LINK_TEMPLATE = "https://t.me/";

    @Override
    public Volunteer getByChatId(long chatId) throws EntityNotFoundException {
        return volunteerRepository.findByChatId(chatId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Не существует волонтёра с чатом ID = %d".formatted(chatId)
                ));
    }

    @Override
    public void create(long chatId, String tgUserName) {
        if (!volunteerRepository.existsByChatId(chatId)) {
            volunteerRepository.saveAndFlush(
                    volunteerMapper.volunteer(chatId, TG_LINK_TEMPLATE.concat(tgUserName))
            );
        }
    }

    @Override
    public List<Volunteer> getVolunteerList() {
        return volunteerRepository.findAll();
    }

    @Override
    public void saveBirthday(long chatId, Date birthday) throws EntityNotFoundException {
        Volunteer volunteer = getByChatId(chatId);
        volunteer.setBirthday(birthday);
        volunteerRepository.saveAndFlush(volunteer);
    }

    @Override
    public void saveCity(long chatId, String city) throws EntityNotFoundException {
        Volunteer volunteer = getByChatId(chatId);
        volunteer.setCity(city);
        volunteerRepository.saveAndFlush(volunteer);
    }

    @Override
    public void saveClothingSize(long chatId, EClothingSize eClothingSize) throws EntityNotFoundException {
        Volunteer volunteer = getByChatId(chatId);
        volunteer.setClothingSize(eClothingSize);
        volunteerRepository.saveAndFlush(volunteer);
    }

    @Override
    public void saveEducationInstitution(long chatId, String educationInstitution) throws EntityNotFoundException {
        Volunteer volunteer = getByChatId(chatId);
        volunteer.setEducationInstitution(educationInstitution);
        volunteerRepository.saveAndFlush(volunteer);
    }

    @Override
    public void saveEducation(long chatId, EEducationStatus eEducationStatus) throws EntityNotFoundException {
        Volunteer volunteer = getByChatId(chatId);
        volunteer.setEducationStatus(eEducationStatus);
        volunteerRepository.saveAndFlush(volunteer);
    }

    @Override
    public void saveEmail(long chatId, String email) throws EntityNotFoundException {
        Volunteer volunteer = getByChatId(chatId);
        volunteer.setEmail(email);
        volunteerRepository.saveAndFlush(volunteer);
    }

    @Override
    public void saveExperience(long chatId, String experience) throws EntityNotFoundException {
        Volunteer volunteer = getByChatId(chatId);
        volunteer.setExperience(experience);
        volunteerRepository.saveAndFlush(volunteer);
    }

    @Override
    public void saveFullName(long chatId, String fullName) throws EntityNotFoundException {
        Volunteer volunteer = getByChatId(chatId);
        volunteer.setFullName(fullName);
        volunteerRepository.saveAndFlush(volunteer);
    }

    @Override
    public void saveGender(long chatId, EGender eGender) throws EntityNotFoundException {
        Volunteer volunteer = getByChatId(chatId);
        volunteer.setGender(eGender);
        volunteerRepository.saveAndFlush(volunteer);
    }

    @Override
    public void savePhone(long chatId, String phone) throws EntityNotFoundException {
        Volunteer volunteer = getByChatId(chatId);
        volunteer.setPhone(phone);
        volunteerRepository.saveAndFlush(volunteer);
    }

    @Override
    public void saveReason(long chatId, String reason) throws EntityNotFoundException {
        Volunteer volunteer = getByChatId(chatId);
        volunteer.setReason(reason);
        volunteerRepository.saveAndFlush(volunteer);
    }

    @Override
    public void saveVk(long chatId, String vk) throws EntityNotFoundException {
        Volunteer volunteer = getByChatId(chatId);
        volunteer.setVk(vk);
        volunteerRepository.saveAndFlush(volunteer);
    }

    @Override
    public void saveVolunteerId(long chatId, String data) throws EntityNotFoundException {
        Volunteer volunteer = getByChatId(chatId);
        volunteer.setVolunteerId(data);
        volunteerRepository.saveAndFlush(volunteer);
    }

    @Override
    public void updateTgLink(Volunteer volunteer, String tgUserName) {
        String tgLink = TG_LINK_TEMPLATE.concat(tgUserName);
        if (!volunteer.getTgLink().equals(tgLink)) {
            volunteer.setTgLink(tgLink);
            volunteerRepository.save(volunteer);
        }
    }

    @Override
    public void flush() {
        volunteerRepository.flush();
    }
}
