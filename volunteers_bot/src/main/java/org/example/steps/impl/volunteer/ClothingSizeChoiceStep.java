package org.example.steps.impl.volunteer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ButtonDto;
import org.example.dto.MessageDto;
import org.example.dto.ResultDto;
import org.example.entities.ChatHash;
import org.example.enums.EClothingSize;
import org.example.exceptions.EntityNotFoundException;
import org.example.mappers.KeyboardMapper;
import org.example.services.VolunteerService;
import org.example.steps.ChoiceStep;
import org.example.utils.StepUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClothingSizeChoiceStep extends ChoiceStep {
    private final VolunteerService volunteerService;
    private final KeyboardMapper keyboardMapper;
    private static final String PREPARE_MESSAGE_TEXT = "Укажите ваш <b>размер одежды</b>:";

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) throws EntityNotFoundException {
        StepUtil.sendPrepareMessageWithInlineKeyBoard(
                chatHash,
                PREPARE_MESSAGE_TEXT,
                keyboardMapper.keyboardDto(chatHash, getClothingSizeButtonDtoList()),
                sender
        );
    }

    @Override
    protected ResultDto isValidData(MessageDto messageDto) {
        if (!isCallback(messageDto.getEMessage())) {
            return new ResultDto(false, EXCEPTION_MESSAGE_TEXT);
        }

        try {
            EClothingSize.valueOf(messageDto.getData());
            return new ResultDto(true);
        } catch (IllegalArgumentException ignored) {
        }

        return new ResultDto(false, EXCEPTION_MESSAGE_TEXT);
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) throws EntityNotFoundException {
        EClothingSize eClothingSize = EClothingSize.valueOf(data);
        volunteerService.saveClothingSize(chatHash.getId(), eClothingSize);
        sendFinishMessage(chatHash, sender, getAnswerMessageText(eClothingSize.getValue()));
        return 0;
    }

    private String getAnswerMessageText(String clothingSize) {
        return "Ваш размер одежды: <b>".concat(clothingSize).concat("</b>");
    }

    private List<ButtonDto> getClothingSizeButtonDtoList() {
        EClothingSize[] eClothingSizeArray = EClothingSize.values();
        List<ButtonDto> buttonDtoList = new ArrayList<>();
        for (EClothingSize eClothingSize : eClothingSizeArray) {
            buttonDtoList.add(new ButtonDto(eClothingSize.toString(), eClothingSize.getValue()));
        }

        return buttonDtoList;
    }
}
