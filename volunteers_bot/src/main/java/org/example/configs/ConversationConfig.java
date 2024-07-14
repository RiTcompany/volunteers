package org.example.configs;

import org.example.enums.EConversation;
import org.example.enums.EConversationStep;
import org.example.repositories.ConversationRepository;
import org.example.services.ConversationStepService;
import org.example.services.impl.ConversationStepServiceImpl;
import org.example.steps.CityChoiceStep;
import org.example.steps.CityInputStep;
import org.example.steps.ConversationStep;
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

    @Bean
    public ConversationStepService conversation() {
        return new ConversationStepServiceImpl(conversationStepGraph(), conversationStartStepMap());
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
        }};
        buildRegisterConversationStepGraph(conversationStepMap);
        return conversationStepMap;
    }

    private void buildRegisterConversationStepGraph(Map<EConversationStep, ConversationStep> conversationStepMap) {
        buildConversationStepGraphNode(conversationStepMap, EConversationStep.CITY_CHOICE, new ArrayList<>() {{
            add(EConversationStep.CITY_INPUT);
            add(null);
        }});
        buildConversationStepGraphNode(conversationStepMap, EConversationStep.CITY_INPUT, new ArrayList<>() {{
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
