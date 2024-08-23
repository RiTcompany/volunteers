package org.example.conversation.impl;

import org.example.conversation.AConversation;
import org.example.enums.EConversationStep;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ParentRegistrationConversation extends AConversation {
    private static final EConversationStep START_STEP = EConversationStep.PARENT_FULL_NAME_INPUT;
    private static final String FINISH_MESSAGE = "Благодарим вас за регистрацию!";

    public ParentRegistrationConversation() {
        super(completeStepGraph(), START_STEP, FINISH_MESSAGE);
    }

    private static Map<EConversationStep, List<EConversationStep>> completeStepGraph() {
        return new HashMap<>() {{
            put(EConversationStep.PARENT_FULL_NAME_INPUT, new ArrayList<>() {{
                add(EConversationStep.PARENT_BIRTHDAY_INPUT);
            }});
            put(EConversationStep.PARENT_BIRTHDAY_INPUT, new ArrayList<>() {{
                add(EConversationStep.PARENT_REGISTER_PLACE_INPUT);
            }});
            put(EConversationStep.PARENT_REGISTER_PLACE_INPUT, new ArrayList<>() {{
                add(EConversationStep.CHILD_FULL_NAME_INPUT);
            }});
            put(EConversationStep.CHILD_FULL_NAME_INPUT, new ArrayList<>() {{
                add(EConversationStep.CHILD_BIRTHDAY_INPUT);
            }});
            put(EConversationStep.CHILD_BIRTHDAY_INPUT, new ArrayList<>() {{
                add(EConversationStep.CHILD_REGISTER_PLACE_INPUT);
            }});
            put(EConversationStep.CHILD_REGISTER_PLACE_INPUT, new ArrayList<>() {{
                add(null);
            }});
        }};
    }
}
