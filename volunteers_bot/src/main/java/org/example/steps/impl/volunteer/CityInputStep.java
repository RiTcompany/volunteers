package org.example.steps.impl.volunteer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ResultDto;
import org.example.entities.ChatHash;
import org.example.exceptions.EntityNotFoundException;
import org.example.repositories.CityRepository;
import org.example.services.VolunteerService;
import org.example.steps.InputStep;
import org.example.utils.StepUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
@Component
@RequiredArgsConstructor
public class CityInputStep extends InputStep {
    private final VolunteerService volunteerService;
    private final CityRepository cityRepository;
    private static final String PREPARE_MESSAGE_TEXT = """
            Мероприятия проходит очно в Санкт-Петербурге. Если вы подтверждаете, что сможете участвовать в них, то продолжите регистрацию

            Введите ваш <b>город</b>:""";

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) throws EntityNotFoundException {
        StepUtil.sendPrepareMessageOnlyText(chatHash, PREPARE_MESSAGE_TEXT, sender);
    }

    @Override
    protected ResultDto isValidData(String city) {
        if (!cityRepository.existsByName(setCityRegister(city))) {
            return new ResultDto(false, "Такого города не существует в РФ. Проверьте правильность написания и попробуйте снова");
        }

        return new ResultDto(true);
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) throws EntityNotFoundException {
        volunteerService.saveCity(chatHash.getId(), setCityRegister(data));
        sendFinishMessage(chatHash, sender, getAnswerMessageText(setCityRegister(data)));
        return 0;
    }

    private String getAnswerMessageText(String answer) {
        return "Ваш город: <b>".concat(answer).concat("</b>");
    }

    private String setCityRegister(String city) {
        return city.substring(0, 1).toUpperCase().concat(city.substring(1).toLowerCase());
    }
}
