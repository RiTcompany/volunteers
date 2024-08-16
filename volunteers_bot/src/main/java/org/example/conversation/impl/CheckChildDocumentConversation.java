package org.example.conversation.impl;

import org.example.conversation.AConversation;
import org.example.enums.EConversationStep;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CheckChildDocumentConversation extends AConversation {
    public CheckChildDocumentConversation() {
        super(
                completeStepGraph(),
                EConversationStep.CHILD_DOCUMENT_CHECK_CHOICE,
                "Проверка документа завершена"
        );
    }

    private static Map<EConversationStep, List<EConversationStep>> completeStepGraph() {
        return new HashMap<>() {{
            put(EConversationStep.CHILD_DOCUMENT_CHECK_CHOICE, new ArrayList<>() {{
                add(EConversationStep.CHILD_DOCUMENT_FAIL_MESSAGE_INPUT);
                add(null);
            }});
            put(EConversationStep.CHILD_DOCUMENT_FAIL_MESSAGE_INPUT, new ArrayList<>() {{
                add(null);
            }});
        }};
    }
}
