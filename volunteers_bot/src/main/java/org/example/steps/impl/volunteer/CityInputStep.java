package org.example.steps.impl.volunteer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.EntityNotFoundException;
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
    protected String getPrepareMessageText() {
        return CityInputStep.PREPARE_MESSAGE_TEXT;
    }

    @Override
    protected ResultDto isValidData(String city) {
        if (!CityUtil.isExistsRussianCity(city)) {
            return new ResultDto(false, "Такого города не существует в РФ. Проверьте правильность написания и попробуйте снова");
        }

        return new ResultDto(true);
    }

    @Override
    protected void saveData(long chatId, String city) throws EntityNotFoundException {
        Volunteer volunteer = volunteerService.getByChatId(chatId);
        volunteer.setCity(city);
        volunteerService.saveAndFlush(volunteer);
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) {
        sendFinishMessage(chatHash, sender, getAnswerMessageText(data));
        return 0;
    }

    private String getAnswerMessageText(String answer) {
        return "Ваш город: <b>".concat(CityUtil.setCityRegister(answer)).concat("</b>");
    }
}
