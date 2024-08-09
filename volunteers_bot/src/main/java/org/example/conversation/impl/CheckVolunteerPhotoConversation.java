package org.example.conversation.impl;

import org.example.conversation.AConversation;
import org.example.enums.EConversationStep;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CheckVolunteerPhotoConversation extends AConversation {
    public CheckVolunteerPhotoConversation() {
        super(
                completeStepGraph(),
                EConversationStep.VOLUNTEER_PHOTO_CHECK_CHOICE,
                "Проверка фотографии завершена"
        );
    }

    private static Map<EConversationStep, List<EConversationStep>> completeStepGraph() {
        return new HashMap<>() {{
            put(EConversationStep.VOLUNTEER_PHOTO_CHECK_CHOICE, new ArrayList<>() {{
                add(EConversationStep.VOLUNTEER_PHOTO_FAIL_MESSAGE_INPUT);
                add(null);
            }});
            put(EConversationStep.VOLUNTEER_PHOTO_FAIL_MESSAGE_INPUT, new ArrayList<>() {{
                add(null);
            }});
        }};
    }
}
