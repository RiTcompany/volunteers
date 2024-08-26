package org.example.services;

import org.example.entities.Volunteer;
import org.example.enums.EAnorak;
import org.example.enums.EClothingSize;
import org.example.enums.EEducationStatus;
import org.example.enums.EGender;
import org.example.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface VolunteerService {
    Volunteer getByChatId(long chatId) throws EntityNotFoundException;

    void create(long chatId, String tgUserName);

    List<Volunteer> findAll();

    void saveBirthday(long chatId, Date birthday) throws EntityNotFoundException;

    void saveCity(long chatId, String city) throws EntityNotFoundException;

    void saveClothingSize(long chatId, EClothingSize eClothingSize) throws EntityNotFoundException;

    void saveEducationInstitution(long chatId, String educationInstitution) throws EntityNotFoundException;

    void saveEducationStatus(long chatId, EEducationStatus eEducationStatus) throws EntityNotFoundException;

    void saveEducationSpeciality(long chatId, String educationSpeciality) throws EntityNotFoundException;

    void saveEmail(long chatId, String email) throws EntityNotFoundException;

    void saveExperience(long chatId, String experience) throws EntityNotFoundException;

    void saveFullName(long chatId, String fullName) throws EntityNotFoundException;

    void saveGender(long chatId, EGender eGender) throws EntityNotFoundException;

    void savePhone(long chatId, String phone) throws EntityNotFoundException;

    void saveReason(long chatId, String reason) throws EntityNotFoundException;

    void saveVk(long chatId, String vk) throws EntityNotFoundException;

    void saveVolunteerId(long chatId, String volunteerId) throws EntityNotFoundException;

    void saveAnorakExists(long chatId, Boolean hasAnorak) throws EntityNotFoundException;

    void saveAnorakType(long chatId, EAnorak anorakType) throws EntityNotFoundException;

    void saveSweatshirtExists(long chatId, Boolean hasSweatshirt) throws EntityNotFoundException;

    void saveTShirtExists(long chatId, Boolean hasTShirt) throws EntityNotFoundException;

    void saveSpbDistrict(long chatId, String spbDistrict) throws EntityNotFoundException;

    void updateTgLink(Volunteer volunteer, String tgUserName);

    void flush();
}
