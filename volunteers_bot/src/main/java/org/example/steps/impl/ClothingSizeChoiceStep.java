package org.example.steps.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.enums.EClothingSize;
import org.example.pojo.dto.ButtonDto;
import org.example.pojo.dto.MessageDto;
import org.example.pojo.entities.ChatHash;
import org.example.services.VolunteerService;
import org.example.steps.ChoiceStep;
import org.example.utils.ButtonUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClothingSizeChoiceStep extends ChoiceStep {
    private final VolunteerService volunteerService;
    private static final String PREPARE_MESSAGE_TEXT = "Укажите ваш <b>размер одежды</b>:";
    private final static List<ButtonDto> buttonDtoList;

    static {
        buttonDtoList = ButtonUtil.clothingSizeButtonList();
    }

    @Override
    public int execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
        int stepIndex = super.execute(chatHash, messageDto, sender);
        if (stepIndex != 0) return stepIndex;

        String clothingSize = messageDto.getData();
        try {
            EClothingSize eClothingSize = EClothingSize.valueOf(clothingSize);
            finishStep(chatHash, sender, getAnswerMessageText(eClothingSize.getEClothingSizeStr()));
            saveClothingSize(chatHash.getId(), eClothingSize);
            return 0;
        } catch (IllegalArgumentException e) {
            log.error("Chat ID={} Incorrect clothingSize choice: {}", chatHash.getId(), clothingSize);
            return handleIllegalUserAction(messageDto, sender, EXCEPTION_MESSAGE_TEXT);
        }
    }

    @Override
    protected void setButtonList() {
        setButtonDtoList(buttonDtoList);
    }

    @Override
    protected String getPREPARE_MESSAGE_TEXT() {
        return PREPARE_MESSAGE_TEXT;
    }

    private void saveClothingSize(long chatId, EClothingSize eClothingSize) {
//        Volunteer volunteer = volunteerService.getVolunteerByChatId(chatId);
//        volunteer.setClothingSize(eClothingSize);
//        volunteerService.saveAndFlushVolunteer(volunteer);
    }

    private String getAnswerMessageText(String clothingSize) {
        return "Ваш размер одежды: <b>".concat(clothingSize).concat("</b>");
    }
}
