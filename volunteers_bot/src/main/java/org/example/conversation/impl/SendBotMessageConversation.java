package org.example.conversation.impl;

import org.example.conversation.AConversation;
import org.example.enums.EConversationStep;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SendBotMessageConversation extends AConversation {
    private static final EConversationStep START_STEP = EConversationStep.BOT_MESSAGE_TEXT_INPUT;
    private static final String FINISH_MESSAGE = "Проверка документа завершена";

    public SendBotMessageConversation() {
        super(completeStepGraph(), START_STEP, FINISH_MESSAGE);
    }

    private static Map<EConversationStep, List<EConversationStep>> completeStepGraph() {
        return new HashMap<>() {{
            put(EConversationStep.BOT_MESSAGE_TEXT_INPUT, new ArrayList<>() {{
                add(EConversationStep.BOT_MESSAGE_BUTTON_ADD_CHOICE);
            }});
            put(EConversationStep.BOT_MESSAGE_BUTTON_ADD_CHOICE, new ArrayList<>() {{
                add(EConversationStep.BOT_MESSAGE_BUTTON_INPUT);
                add(EConversationStep.SEND_BOT_MESSAGE_CHOICE);
            }});
            put(EConversationStep.BOT_MESSAGE_BUTTON_INPUT, new ArrayList<>() {{
                add(EConversationStep.BOT_MESSAGE_BUTTON_ADD_CHOICE);
            }});
            put(EConversationStep.SEND_BOT_MESSAGE_CHOICE, new ArrayList<>() {{
                add(null);
            }});
        }};
    }
}
