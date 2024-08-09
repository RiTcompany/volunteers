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
    public ParentRegistrationConversation() {
        super(
                completeStepGraph(),
                EConversationStep.PARENT_FULL_NAME_INPUT,
                "Благодарим вас за регистрацию!"
        );
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
