package org.example.steps.impl.volunteer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ResultDto;
import org.example.entities.ChatHash;
import org.example.exceptions.EntityNotFoundException;
import org.example.services.VolunteerService;
import org.example.steps.InputStep;
import org.example.utils.StepUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class VkInputStep extends InputStep {
    private final VolunteerService volunteerService;
    private static final String PREPARE_MESSAGE_TEXT = "Введите ссылку на ваш <b>Профиль ВКонтакте</b>:";
    private static final Pattern pattern = Pattern.compile("^https://vk.com/[a-z0-9_.]+$");

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) throws EntityNotFoundException {
        StepUtil.sendPrepareMessageOnlyText(chatHash, PREPARE_MESSAGE_TEXT, sender);
    }

    protected ResultDto isValidData(String data) {
        if (!pattern.matcher(data).matches()) {
            return new ResultDto(false, "Некорректная ссылка. Ссылка должна иметь вид https://vk.com/{имя}");
        }

        return new ResultDto(true);
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) throws EntityNotFoundException {
        volunteerService.saveVk(chatHash.getId(), data);
        sendFinishMessage(chatHash, sender, getAnswerMessageText(data));
        return 0;
    }

    private String getAnswerMessageText(String vk) {
        return "Ваша ссылка: <b>".concat(vk).concat("</b>");
    }
}
