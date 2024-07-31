package org.example.steps.impl.volunteer;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ButtonDto;
import org.example.enums.EClothingSize;
import org.example.exceptions.EntityNotFoundException;
import org.example.dto.ResultDto;
import org.example.entities.ChatHash;
import org.example.entities.Volunteer;
import org.example.services.VolunteerService;
import org.example.steps.ChoiceStep;
import org.example.utils.ButtonUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClothingSizeChoiceStep extends ChoiceStep {
    private final VolunteerService volunteerService;
    private static final String PREPARE_MESSAGE_TEXT = "Укажите ваш <b>размер одежды</b>:";

    @PostConstruct
    public void init() {
        setButtonDtoList(getClothingSizeButtonDtoList());
    }

    @Override
    protected String getPrepareMessageText() {
        return PREPARE_MESSAGE_TEXT;
    }

    @Override
    protected ResultDto isValidData(String data) {
        try {
            EClothingSize.valueOf(data);
            return new ResultDto(true);
        } catch (IllegalArgumentException e) {
            return new ResultDto(false, EXCEPTION_MESSAGE_TEXT);
        }
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) throws EntityNotFoundException {
        EClothingSize eClothingSize = EClothingSize.valueOf(data);
        saveClothingSize(chatHash.getId(), eClothingSize);
        sendFinishMessage(chatHash, sender, getAnswerMessageText(eClothingSize.getEClothingSizeStr()));
        return 0;
    }

    private void saveClothingSize(long chatId, EClothingSize eClothingSize) throws EntityNotFoundException {
        Volunteer volunteer = volunteerService.getByChatId(chatId);
        volunteer.setClothingSize(eClothingSize);
        volunteerService.saveAndFlush(volunteer);
    }

    private String getAnswerMessageText(String clothingSize) {
        return "Ваш размер одежды: <b>".concat(clothingSize).concat("</b>");
    }

    private List<ButtonDto> getClothingSizeButtonDtoList() {
        EClothingSize[] eClothingSizeArray = EClothingSize.values();
        List<ButtonDto> buttonDtoList = new ArrayList<>();
        for (int i = 0; i < eClothingSizeArray.length; i++) {
            buttonDtoList.add(new ButtonDto(eClothingSizeArray[i].toString(), eClothingSizeArray[i].getEClothingSizeStr(), i));
        }

        return buttonDtoList;
    }
}
