package org.example.steps.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.enums.EConversationStep;
import org.example.enums.EEducationStatus;
import org.example.pojo.dto.ButtonDto;
import org.example.pojo.dto.MessageDto;
import org.example.pojo.entities.ChatHash;
import org.example.pojo.entities.Volonteer;
import org.example.services.VolonteerService;
import org.example.steps.ChoiceStep;
import org.example.utils.ButtonUtil;
import org.example.utils.EducationUtil;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;

@Slf4j
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class EducationInstitutionChoiceStep extends ChoiceStep {
    private final VolonteerService volonteerService;
    @Getter(AccessLevel.PROTECTED)
    private static final String PREPARE_MESSAGE_TEXT = "Укажите ваше <b>учебное заведение</b>: ";
    private static final String FOUND_INSTITUTION_MESSAGE_TEXT_TEMPLATE = "Ваше учебное заведение: <b>";
    private static final String NOT_FOUND_INSTITUTION_MESSAGE_TEXT = "Ваше учебное заведение: ожидается";

    @Override
    public EConversationStep execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
        EConversationStep eConversationStep = super.execute(chatHash, messageDto, sender);
        if (eConversationStep != null) return eConversationStep;

        String educationInstitution = messageDto.getData();
        EEducationStatus eEducationStatus = getEEducationStatus(chatHash.getId()); // TODO : дважды запрашиваю из бд, надо бы исправить
        if (isValidEducationInstitution(educationInstitution, eEducationStatus)) {
            if (EducationUtil.ANOTHER_ANSWER.equals(educationInstitution)) {
                finishStep(chatHash, sender, NOT_FOUND_INSTITUTION_MESSAGE_TEXT);
                return eConversationStepList.get(0);
            }

            finishStep(chatHash, sender, FOUND_INSTITUTION_MESSAGE_TEXT_TEMPLATE.concat(educationInstitution).concat("</b>"));
            saveEducationInstitution(chatHash.getId(), educationInstitution);
            return eConversationStepList.get(1);
        } else {
            log.error("Chat ID={} Incorrect gender choice: {}", chatHash.getId(), educationInstitution);
            return handleIllegalUserAction(chatHash, messageDto, sender, EXCEPTION_MESSAGE_TEXT);
        }
    }

    @Override
    protected void setButtonList(long chatId) {
        EEducationStatus eEducationStatus = getEEducationStatus(chatId);
        setButtonDtoList(getButtonDtoList(eEducationStatus));
    }

    private boolean isValidEducationInstitution(String educationInstitution, EEducationStatus eEducationStatus) {
        List<String> educationInstitutionList = EducationUtil.getEducationInstitutionList(eEducationStatus);
        return educationInstitutionList.contains(educationInstitution);
    }

    private void saveEducationInstitution(long chatId, String educationInstitution) {
        Volonteer volonteer = volonteerService.getVolonteerByChatId(chatId);
        volonteer.setEducationInstitution(educationInstitution);
        volonteerService.saveAndFlushVolonteer(volonteer);
    }

    private List<ButtonDto> getButtonDtoList(EEducationStatus eEducationStatus) {
        return ButtonUtil.educationInstitutionButtonList(eEducationStatus);
    }

    private EEducationStatus getEEducationStatus(long chatId) {
        return volonteerService.getVolonteerByChatId(chatId).getEducationStatus();
    }
}
