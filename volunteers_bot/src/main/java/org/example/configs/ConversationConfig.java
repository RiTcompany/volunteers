package org.example.configs;

import org.example.enums.EConversation;
import org.example.enums.EConversationStep;
import org.example.steps.ConversationStep;
import org.example.steps.impl.BirthdayInputStep;
import org.example.steps.impl.ChildDocumentStep;
import org.example.steps.impl.CityChoiceStep;
import org.example.steps.impl.CityInputStep;
import org.example.steps.impl.EducationInstitutionChoiceStep;
import org.example.steps.impl.EducationInstitutionInputStep;
import org.example.steps.impl.EducationStatusChoiceStep;
import org.example.steps.impl.FullNameInputStep;
import org.example.steps.impl.GenderChoiceStep;
import org.example.steps.impl.PhoneInputStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class ConversationConfig {
    @Bean
    public Map<EConversation, Map<EConversationStep, List<EConversationStep>>> conversationStepGraph() {
        Map<EConversation, Map<EConversationStep, List<EConversationStep>>> conversationMap = new HashMap<>();
        conversationMap.put(EConversation.REGISTER, registerConversationStepGraph());
        return conversationMap;
    }

    @Bean
    public Map<EConversation, EConversationStep> conversationStartStepMap() {
        Map<EConversation, EConversationStep> conversationStartStepMap = new HashMap<>();
        conversationStartStepMap.put(EConversation.REGISTER, EConversationStep.CITY_CHOICE);
        return conversationStartStepMap;
    }

    @Bean
    public Map<EConversationStep, ConversationStep> conversationStepMap(
            @Autowired CityChoiceStep cityChoiceStep,
            @Autowired CityInputStep cityInputStep,
            @Autowired BirthdayInputStep birthdayInputStep,
            @Autowired ChildDocumentStep childDocumentStep,
            @Autowired FullNameInputStep fullNameInputStep,
            @Autowired GenderChoiceStep genderChoiceStep,
            @Autowired PhoneInputStep phoneInputStep,
            @Autowired EducationStatusChoiceStep educationStatusChoiceStep,
            @Autowired EducationInstitutionChoiceStep educationInstitutionChoiceStep,
            @Autowired EducationInstitutionInputStep educationInstitutionInputStep
    ) {
        Map<EConversationStep, ConversationStep> conversationStepMap = new HashMap<>();
        conversationStepMap.put(EConversationStep.CITY_CHOICE, cityChoiceStep);
        conversationStepMap.put(EConversationStep.CITY_INPUT, cityInputStep);
        conversationStepMap.put(EConversationStep.BIRTHDAY_INPUT, birthdayInputStep);
        conversationStepMap.put(EConversationStep.CHILD_DOCUMENT, childDocumentStep);
        conversationStepMap.put(EConversationStep.FULL_NAME_INPUT, fullNameInputStep);
        conversationStepMap.put(EConversationStep.GENDER_CHOICE, genderChoiceStep);
        conversationStepMap.put(EConversationStep.PHONE_INPUT, phoneInputStep);
        conversationStepMap.put(EConversationStep.EDUCATION_STATUS_CHOICE, educationStatusChoiceStep);
        conversationStepMap.put(EConversationStep.EDUCATION_INSTITUTION_CHOICE, educationInstitutionChoiceStep);
        conversationStepMap.put(EConversationStep.EDUCATION_INSTITUTION_INPUT, educationInstitutionInputStep);
        return conversationStepMap;
    }

    private Map<EConversationStep, List<EConversationStep>> registerConversationStepGraph() {
        return new HashMap<>() {{
            put(EConversationStep.CITY_CHOICE, new ArrayList<>() {{
                add(EConversationStep.CITY_INPUT);
                add(EConversationStep.BIRTHDAY_INPUT);
            }});
            put(EConversationStep.CITY_INPUT, new ArrayList<>() {{
                add(EConversationStep.BIRTHDAY_INPUT);
            }});
            put(EConversationStep.BIRTHDAY_INPUT, new ArrayList<>() {{
                add(EConversationStep.CHILD_DOCUMENT);
                add(EConversationStep.FULL_NAME_INPUT);
            }});
            put(EConversationStep.CHILD_DOCUMENT, new ArrayList<>() {{
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
                add(null);
            }});
            put(EConversationStep.EDUCATION_INSTITUTION_INPUT, new ArrayList<>() {{
                add(null);
            }});
        }};
    }
}
