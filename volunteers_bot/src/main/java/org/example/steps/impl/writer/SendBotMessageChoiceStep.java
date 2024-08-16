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
import org.example.services.BotUserService;
import org.example.services.GroupChatService;
import org.example.services.VolunteerService;
import org.example.steps.ChoiceStep;
import org.example.utils.ButtonUtil;
import org.example.utils.MessageUtil;
import org.example.utils.StepUtil;
import org.example.utils.ValidUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SendBotMessageChoiceStep extends ChoiceStep {
    private final VolunteerService volunteerService;
    private final BotMessageService botMessageService;
    private final BotUserService botUserService;
    private final KeyboardMapper keyboardMapper;
    private final GroupChatService groupChatService;
    private static final String PREPARE_MESSAGE_TEXT = "Ваше сообщение:";
    private static final String CHOICE_MESSAGE_TEXT = "Вы хотите отправить данное сообщение?";


    @Override
    protected ResultDto isValidData(MessageDto messageDto) throws EntityNotFoundException {
        return ValidUtil.isValidYesNoChoice(messageDto, EXCEPTION_MESSAGE_TEXT);
    }

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) throws EntityNotFoundException {
        StepUtil.sendPrepareMessageOnlyText(chatHash, PREPARE_MESSAGE_TEXT, sender);
        sendMessageToCheck(chatHash, sender);
        StepUtil.sendPrepareMessageWithInlineKeyBoard(
                chatHash,
                CHOICE_MESSAGE_TEXT,
                keyboardMapper.keyboardDto(chatHash, ButtonUtil.yesNoButtonList()),
                sender
        );
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) throws EntityNotFoundException {
        long userId = botUserService.getByChatIdAndRole(chatHash.getId(), ERole.ROLE_WRITER).getId();
        BotMessage botMessage = botMessageService.getProcessedMessageByUserId(userId);

        if (EYesNo.NO.toString().equals(data)) {
            botMessageService.deleteButtons(botMessage);
            botMessageService.delete(botMessage);
        } else {
            sendMessages(botMessage, chatHash, sender);
        }

        return 0;
    }

    private void sendMessageToCheck(ChatHash chatHash, AbsSender sender) throws EntityNotFoundException {
        long userId = botUserService.getByChatIdAndRole(chatHash.getId(), ERole.ROLE_WRITER).getId();
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

//        sendMessageToGroups(messageBuilder, sender);
        sendMessageToVolunteers(messageBuilder, sender);

        botMessageService.saveSentStatus(botMessage);
    }

    private void sendMessageToVolunteers(MessageBuilder messageBuilder, AbsSender sender) {
        volunteerService.findAll().forEach(volunteer -> {
                    Message message = MessageUtil.sendMessage(
                            messageBuilder.sendMessage(volunteer.getChatId()), sender
                    );
                    if (message != null) {
                        volunteerService.updateTgLink(volunteer, message.getChat().getUserName());
                    }
                }
        );
        volunteerService.flush();
    }

    private void sendMessageToGroups(MessageBuilder messageBuilder, AbsSender sender) {
        groupChatService.findAll().forEach(groupChat -> MessageUtil.sendMessage(
                messageBuilder.sendMessage(groupChat.getChatId()), sender
        ));
    }
}
