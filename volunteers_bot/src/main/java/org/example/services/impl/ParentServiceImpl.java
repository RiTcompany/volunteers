package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.entities.Parent;
import org.example.exceptions.EntityNotFoundException;
import org.example.mappers.ParentMapper;
import org.example.repositories.ParentRepository;
import org.example.services.ParentService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class ParentServiceImpl implements ParentService {
    private final ParentRepository parentRepository;
    private final ParentMapper parentMapper;

    @Override
    public Parent getByChatId(long chatId) throws EntityNotFoundException {
        return parentRepository.findByChatId(chatId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Не существует родителя с чатом ID = %d".formatted(chatId)
                ));
    }

    @Override
    public void saveAndFlush(Parent parent) {
        parentRepository.saveAndFlush(parent);
    }

    @Override
    public void create(long chatId) {
        if (!parentRepository.existsByChatId(chatId)) {
            parentRepository.saveAndFlush(parentMapper.parent(chatId));
        }
    }

    @Override
    public void saveChildBirthday(long chatId, Date birthday) throws EntityNotFoundException {
        Parent parent = getByChatId(chatId);
        parent.setChildBirthday(birthday);
        parentRepository.saveAndFlush(parent);
    }

    @Override
    public void saveChildFullName(long chatId, String fullName) throws EntityNotFoundException {
        Parent parent = getByChatId(chatId);
        parent.setChildFullName(fullName);
        parentRepository.saveAndFlush(parent);
    }

    @Override
    public void saveChildRegisterPlace(long chatId, String registerPlace) throws EntityNotFoundException {
        Parent parent = getByChatId(chatId);
        parent.setChildRegisterPlace(registerPlace);
        parentRepository.saveAndFlush(parent);
    }

    @Override
    public void saveParentBirthday(long chatId, Date birthday) throws EntityNotFoundException {
        Parent parent = getByChatId(chatId);
        parent.setBirthday(birthday);
        parentRepository.saveAndFlush(parent);
    }

    @Override
    public void saveParentFullName(long chatId, String fullName) throws EntityNotFoundException {
        Parent parent = getByChatId(chatId);
        parent.setFullName(fullName);
        parentRepository.saveAndFlush(parent);
    }

    @Override
    public void saveParentRegisterPlace(long chatId, String registerPlace) throws EntityNotFoundException {
        Parent parent = getByChatId(chatId);
        parent.setRegisterPlace(registerPlace);
        parentRepository.saveAndFlush(parent);
    }
}
