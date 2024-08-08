package org.example.services;

import org.example.entities.Parent;
import org.example.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public interface ParentService {
    Parent getByChatId(long chatId) throws EntityNotFoundException;

    void saveAndFlush(Parent parent);

    void create(long chatId);

    void saveChildBirthday(long chatId, Date birthday) throws EntityNotFoundException;

    void saveChildFullName(long chatId, String fullName) throws EntityNotFoundException;

    void saveChildRegisterPlace(long chatId, String registerPlace) throws EntityNotFoundException;

    void saveParentBirthday(long chatId, Date birthday) throws EntityNotFoundException;

    void saveParentFullName(long chatId, String fullName) throws EntityNotFoundException;

    void saveParentRegisterPlace(long chatId, String registerPlace) throws EntityNotFoundException;
}
