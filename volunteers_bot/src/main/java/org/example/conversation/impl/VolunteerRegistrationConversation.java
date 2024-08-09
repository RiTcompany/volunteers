package org.example.conversation.impl;

import org.example.conversation.AConversation;
import org.example.enums.EConversationStep;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class VolunteerRegistrationConversation extends AConversation {
    public VolunteerRegistrationConversation() {
        super(
                completeStepGraph(),
                EConversationStep.CITY_CHOICE,
                "Благодарим тебя за регистрацию! Теперь ты можешь выбирать мероприятия для участия!"
        );
    }

    private static Map<EConversationStep, List<EConversationStep>> completeStepGraph() {
        return new HashMap<>() {{
            put(EConversationStep.CITY_CHOICE, new ArrayList<>() {{
                add(EConversationStep.CITY_INPUT);
                add(EConversationStep.BIRTHDAY_INPUT);
            }});
            put(EConversationStep.CITY_INPUT, new ArrayList<>() {{
                add(EConversationStep.BIRTHDAY_INPUT);
            }});
            put(EConversationStep.BIRTHDAY_INPUT, new ArrayList<>() {{
                add(EConversationStep.CHILD_DOCUMENT_SEND);
                add(EConversationStep.FULL_NAME_INPUT);
            }});
            put(EConversationStep.CHILD_DOCUMENT_SEND, new ArrayList<>() {{
                add(EConversationStep.FULL_NAME_INPUT);
            }});
            put(EConversationStep.FULL_NAME_INPUT, new ArrayList<>() {{
                add(EConversationStep.GENDER_CHOICE);
            }});
            put(EConversationStep.GENDER_CHOICE, new ArrayList<>() {{
                add(EConversationStep.PHONE_INPUT);
            }});
            put(EConversationStep.PHONE_INPUT, new ArrayList<>() {{
                add(EConversationStep.EDUCATION_STATUS_CHOICE);
            }});
            put(EConversationStep.EDUCATION_STATUS_CHOICE, new ArrayList<>() {{
                add(EConversationStep.EDUCATION_INSTITUTION_CHOICE);
            }});
            put(EConversationStep.EDUCATION_INSTITUTION_CHOICE, new ArrayList<>() {{
                add(EConversationStep.EDUCATION_INSTITUTION_INPUT);
                add(EConversationStep.AGREEMENT_CHOICE);
            }});
            put(EConversationStep.EDUCATION_INSTITUTION_INPUT, new ArrayList<>() {{
                add(EConversationStep.AGREEMENT_CHOICE);
            }});
            put(EConversationStep.AGREEMENT_CHOICE, new ArrayList<>() {{
                add(EConversationStep.VK_INPUT);
            }});
            put(EConversationStep.VK_INPUT, new ArrayList<>() {{
                add(EConversationStep.CLOTHING_SIZE_CHOICE);
            }});
            put(EConversationStep.CLOTHING_SIZE_CHOICE, new ArrayList<>() {{
                add(EConversationStep.REASON_INPUT);
            }});
            put(EConversationStep.REASON_INPUT, new ArrayList<>() {{
                add(EConversationStep.EXPERIENCE_INPUT);
            }});
            put(EConversationStep.EXPERIENCE_INPUT, new ArrayList<>() {{
                add(EConversationStep.PHOTO_SEND);
            }});
            put(EConversationStep.PHOTO_SEND, new ArrayList<>() {{
                add(EConversationStep.EMAIL_INPUT);
            }});
            put(EConversationStep.EMAIL_INPUT, new ArrayList<>() {{
                add(EConversationStep.VOLUNTEER_ID_INPUT);
            }});
            put(EConversationStep.VOLUNTEER_ID_INPUT, new ArrayList<>() {{
                add(null);
            }});
        }};
    }
}
