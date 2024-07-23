package org.example.steps.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.pojo.dto.MessageDto;
import org.example.pojo.dto.ResultDto;
import org.example.pojo.entities.ChatHash;
import org.example.pojo.entities.Volunteer;
import org.example.services.VolunteerService;
import org.example.steps.InputStep;
import org.example.utils.CityUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
@Component
@RequiredArgsConstructor
public class CityInputStep extends InputStep {
    private final VolunteerService volunteerService;
    private static final String PREPARE_MESSAGE_TEXT = """
            Мероприятия проходит очно в Санкт-Петербурге. Если вы подтверждаете, что сможете участвовать в них, то продолжите регистрацию

            Введите ваш <b>город</b>:""";

    @Override
    public int execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
        String city = CityUtil.setCityRegister(messageDto.getData());
        ResultDto result = setCity(chatHash.getId(), city);
        if (result.isDone()) {
            finishStep(chatHash, sender, getAnswerMessageText(city));
            return 0;
        }

        return handleIllegalUserAction(messageDto, sender, result.getMessage());
    }

    @Override
    protected String getPREPARE_MESSAGE_TEXT() {
        return CityInputStep.PREPARE_MESSAGE_TEXT;
    }

    private ResultDto setCity(long chatId, String city) {
        if (!isValidCity(city)) {
            log.error("Chat ID={} incorrect city: {}", chatId, city);
            return new ResultDto(false, "Такого города не существует в РФ. Проверьте правильность написания и попробуйте снова");
        }

        saveCity(chatId, city);
        return new ResultDto(true);
    }

    private boolean isValidCity(String city) {
        return CityUtil.isExistsRussianCity(city);
    }

    private void saveCity(long chatId, String city) {
        Volunteer volunteer = volunteerService.getVolunteerByChatId(chatId);
        volunteer.setCity(city);
        volunteerService.saveAndFlushVolunteer(volunteer);
    }

    private String getAnswerMessageText(String answer) {
        return "Ваш город: <b>".concat(answer).concat("</b>");
    }
}
