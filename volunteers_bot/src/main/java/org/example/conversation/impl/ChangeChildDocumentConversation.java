package org.example.conversation.impl;

import org.example.conversation.AConversation;
import org.example.enums.EConversationStep;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ChangeChildDocumentConversation extends AConversation {
    private static final EConversationStep START_STEP = EConversationStep.CHILD_DOCUMENT_SEND;
    private static final String FINISH_MESSAGE = null;

    public ChangeChildDocumentConversation() {
        super(completeStepGraph(), START_STEP, FINISH_MESSAGE);
    }

    private static Map<EConversationStep, List<EConversationStep>> completeStepGraph() {
        return new HashMap<>() {{
            put(EConversationStep.CHILD_DOCUMENT_SEND, new ArrayList<>() {{
                add(null);
            }});
        }};
    }
}
