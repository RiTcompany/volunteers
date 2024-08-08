package org.example.steps.impl.writer;

import lombok.RequiredArgsConstructor;
import org.example.builders.MessageBuilder;
import org.example.dto.ButtonDto;
import org.example.dto.MessageDto;
import org.example.dto.ResultDto;
import org.example.entities.BotMessage;
import org.example.entities.BotMessageButton;
import org.example.entities.ChatHash;
import org.example.enums.ERole;
import org.example.enums.EYesNo;
import org.example.exceptions.EntityNotFoundException;
import org.example.mappers.KeyboardMapper;
import org.example.services.BotMessageService;
import org.example.services.UserService;
import org.example.services.VolunteerService;
import org.example.steps.ChoiceStep;
import org.example.utils.ButtonUtil;
import org.example.utils.MessageUtil;
import org.example.utils.StepUtil;
import org.example.utils.ValidUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SendBotMessageChoiceStep extends ChoiceStep {
    private final VolunteerService volunteerService;
    private final BotMessageService botMessageService;
    private final UserService userService;
    private final KeyboardMapper keyboardMapper;
    private static final String PREPARE_MESSAGE_TEXT = "Вы хотите отправить данное сообщение?";

    @Override
    protected ResultDto isValidData(MessageDto messageDto) throws EntityNotFoundException {
        return ValidUtil.isValidYesNoChoice(messageDto, EXCEPTION_MESSAGE_TEXT);
    }

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) throws EntityNotFoundException {
        sendMessageToCheck(chatHash, sender);
        StepUtil.sendPrepareMessageWithInlineKeyBoard(
                chatHash,
                PREPARE_MESSAGE_TEXT,
                keyboardMapper.keyboardDto(chatHash, ButtonUtil.yesNoButtonList()),
                sender
        );
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) throws EntityNotFoundException {
        long userId = userService.getByChatIdAndRole(chatHash.getId(), ERole.ROLE_WRITER).getId();
        BotMessage botMessage = botMessageService.getProcessedMessageByUserId(userId);

        System.out.println(data);
        if (EYesNo.NO.toString().equals(data)) {
            botMessageService.deleteButtons(botMessage);
            botMessageService.delete(botMessage);
        } else {
            sendMessages(botMessage, chatHash, sender);
        }

        return 0;
    }

    private void sendMessageToCheck(ChatHash chatHash, AbsSender sender) throws EntityNotFoundException {
        long userId = userService.getByChatIdAndRole(chatHash.getId(), ERole.ROLE_WRITER).getId();
        BotMessage botMessage = botMessageService.getProcessedMessageByUserId(userId);

        StepUtil.sendPrepareMessageWithInlineKeyBoard(
                chatHash,
                botMessage.getText(),
                keyboardMapper.keyboardDto(chatHash, collectButtonList(botMessage)),
                sender
        );
    }

    private List<ButtonDto> collectButtonList(BotMessage botMessage) {
        List<BotMessageButton> botMessageButtonList = botMessageService.getButtonList(botMessage);
        List<ButtonDto> buttonDtoList = new ArrayList<>();
        for (BotMessageButton button : botMessageButtonList) {
            buttonDtoList.add(new ButtonDto(button.getButtonName(), button.getButtonName(), button.getButtonLink()));
        }

        return buttonDtoList;
    }

    private void sendMessages(BotMessage botMessage, ChatHash chatHash, AbsSender sender) {
        MessageBuilder messageBuilder = MessageBuilder.create()
                .setText(botMessage.getText())
                .setInlineKeyBoard(keyboardMapper.keyboardDto(chatHash, collectButtonList(botMessage)));

        volunteerService.getVolunteerChatIdList().forEach(chatId ->
                MessageUtil.sendMessage(messageBuilder.sendMessage(chatId), sender)
        );

        botMessageService.saveSentStatus(botMessage);
    }
}
