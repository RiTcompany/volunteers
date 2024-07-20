package org.example.configs;

import org.example.enums.EConversation;
import org.example.enums.EConversationStep;
import org.example.services.ConversationStepService;
import org.example.services.impl.ConversationStepServiceImpl;
import org.example.steps.impl.BirthdayInputStep;
import org.example.steps.impl.ChildDocumentStep;
import org.example.steps.impl.CityChoiceStep;
import org.example.steps.impl.CityInputStep;
import org.example.steps.ConversationStep;
import org.example.steps.impl.EducationInstitutionChoiceStep;
import org.example.steps.impl.EducationInstitutionInputStep;
import org.example.steps.impl.EducationStatusChoiceStep;
import org.example.steps.impl.FullNameInputStep;
import org.example.steps.impl.GenderChoiceStep;
import org.example.steps.impl.PhoneInputStep;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class ConversationConfig {
    @Autowired
    private ObjectFactory<CityChoiceStep> cityChoiceStepObjectFactory;
    @Autowired
    private ObjectFactory<CityInputStep> cityInputStepObjectFactory;
    @Autowired
    private ObjectFactory<BirthdayInputStep> birthdayStepObjectFactory;
    @Autowired
    private ObjectFactory<ChildDocumentStep> childDocumentStepObjectFactory;
    @Autowired
    private ObjectFactory<FullNameInputStep> fullNameInputStepObjectFactory;
    @Autowired
    private ObjectFactory<GenderChoiceStep> genderChoiceStepObjectFactory;
    @Autowired
    private ObjectFactory<PhoneInputStep> phoneInputStepObjectFactory;
    @Autowired
    private ObjectFactory<EducationStatusChoiceStep> educationStatusChoiceStepObjectFactory;
    @Autowired
    private ObjectFactory<EducationInstitutionChoiceStep> educationInstitutionChoiceStepObjectFactory;
    @Autowired
    private ObjectFactory<EducationInstitutionInputStep> educationInstitutionInputStepObjectFactory;

    @Bean
    public ConversationStepService conversation() {
        return new ConversationStepServiceImpl(
                conversationStepGraph(),
                conversationStartStepMap()
        );
    }

    @Bean
    public Map<EConversation, EConversationStep> conversationStartStepMap() {
        Map<EConversation, EConversationStep> conversationStartStepMap = new HashMap<>();
        conversationStartStepMap.put(EConversation.REGISTER, EConversationStep.CITY_CHOICE);
        return conversationStartStepMap;
    }

    @Bean
    public Map<EConversation, Map<EConversationStep, ConversationStep>> conversationStepGraph() {
        Map<EConversation, Map<EConversationStep, ConversationStep>> conversationMap = new HashMap<>();
        conversationMap.put(EConversation.REGISTER, registerConversationStepGraph());
        return conversationMap;
    }

    private Map<EConversationStep, ConversationStep> registerConversationStepGraph() {
        Map<EConversationStep, ConversationStep> conversationStepMap = new HashMap<>() {{
            put(EConversationStep.CITY_CHOICE, cityChoiceStepObjectFactory.getObject());
            put(EConversationStep.CITY_INPUT, cityInputStepObjectFactory.getObject());
            put(EConversationStep.BIRTHDAY_INPUT, birthdayStepObjectFactory.getObject());
            put(EConversationStep.CHILD_DOCUMENT_SEND, childDocumentStepObjectFactory.getObject());
            put(EConversationStep.FULL_NAME_INPUT, fullNameInputStepObjectFactory.getObject());
            put(EConversationStep.GENDER_CHOICE, genderChoiceStepObjectFactory.getObject());
            put(EConversationStep.PHONE_INPUT, phoneInputStepObjectFactory.getObject());
            put(EConversationStep.EDUCATION_STATUS_CHOICE, educationStatusChoiceStepObjectFactory.getObject());
            put(EConversationStep.EDUCATION_INSTITUTION_CHOICE, educationInstitutionChoiceStepObjectFactory.getObject());
            put(EConversationStep.EDUCATION_INSTITUTION_INPUT, educationInstitutionInputStepObjectFactory.getObject());
        }};
        buildRegisterConversationStepGraph(conversationStepMap);
        return conversationStepMap;
    }

    private void buildRegisterConversationStepGraph(Map<EConversationStep, ConversationStep> conversationStepMap) {
        buildConversationStepGraphNode(conversationStepMap, EConversationStep.CITY_CHOICE, new ArrayList<>() {{
            add(EConversationStep.CITY_INPUT);
            add(EConversationStep.BIRTHDAY_INPUT);
        }});
        buildConversationStepGraphNode(conversationStepMap, EConversationStep.CITY_INPUT, new ArrayList<>() {{
            add(EConversationStep.BIRTHDAY_INPUT);
        }});
        buildConversationStepGraphNode(conversationStepMap, EConversationStep.BIRTHDAY_INPUT, new ArrayList<>() {{
            add(EConversationStep.CHILD_DOCUMENT_SEND);
            add(EConversationStep.FULL_NAME_INPUT);
        }});
        buildConversationStepGraphNode(conversationStepMap, EConversationStep.CHILD_DOCUMENT_SEND, new ArrayList<>() {{
            add(EConversationStep.FULL_NAME_INPUT);
        }});
        buildConversationStepGraphNode(conversationStepMap, EConversationStep.FULL_NAME_INPUT, new ArrayList<>() {{
            add(EConversationStep.GENDER_CHOICE);
        }});
        buildConversationStepGraphNode(conversationStepMap, EConversationStep.GENDER_CHOICE, new ArrayList<>() {{
            add(EConversationStep.PHONE_INPUT);
        }});
        buildConversationStepGraphNode(conversationStepMap, EConversationStep.PHONE_INPUT, new ArrayList<>() {{
            add(EConversationStep.EDUCATION_STATUS_CHOICE);
        }});
        buildConversationStepGraphNode(conversationStepMap, EConversationStep.EDUCATION_STATUS_CHOICE, new ArrayList<>() {{
            add(EConversationStep.EDUCATION_INSTITUTION_CHOICE);
        }});
        buildConversationStepGraphNode(conversationStepMap, EConversationStep.EDUCATION_INSTITUTION_CHOICE, new ArrayList<>() {{
            add(EConversationStep.EDUCATION_INSTITUTION_INPUT);
            add(null);
        }});
        buildConversationStepGraphNode(conversationStepMap, EConversationStep.EDUCATION_INSTITUTION_INPUT, new ArrayList<>() {{
            add(null);
        }});
    }

    private void buildConversationStepGraphNode(
            Map<EConversationStep, ConversationStep> conversationStepMap,
            EConversationStep eParentConversationStep,
            List<EConversationStep> eChilderConversationStepList
    ) {
        ConversationStep parentConversationStep = conversationStepMap.get(eParentConversationStep);
        setNextConversationStepList(parentConversationStep, eChilderConversationStepList);
    }

    private void setNextConversationStepList(
            ConversationStep parentStep, List<EConversationStep> nextConversationStepList
    ) {
        parentStep.setEConversationStepList(nextConversationStepList);
    }
}
