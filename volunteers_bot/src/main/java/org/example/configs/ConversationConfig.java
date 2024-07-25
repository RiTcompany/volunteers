package org.example.configs;

import org.example.enums.EConversation;
import org.example.enums.EConversationStep;
import org.example.steps.ConversationStep;
import org.example.steps.impl.parent.ChildBirthdayInputStep;
import org.example.steps.impl.parent.ChildFullNameInputStep;
import org.example.steps.impl.parent.ChildRegisterPlaceInputStep;
import org.example.steps.impl.parent.ParentBirthdayInputStep;
import org.example.steps.impl.parent.ParentFullNameInputStep;
import org.example.steps.impl.parent.ParentRegisterPlaceInputStep;
import org.example.steps.impl.volunteer.AgreementChoiceStep;
import org.example.steps.impl.volunteer.BirthdayInputStep;
import org.example.steps.impl.volunteer.ChildFileSendStep;
import org.example.steps.impl.volunteer.CityChoiceStep;
import org.example.steps.impl.volunteer.CityInputStep;
import org.example.steps.impl.volunteer.ClothingSizeChoiceStep;
import org.example.steps.impl.volunteer.EducationInstitutionChoiceStep;
import org.example.steps.impl.volunteer.EducationInstitutionInputStep;
import org.example.steps.impl.volunteer.EducationStatusChoiceStep;
import org.example.steps.impl.volunteer.EmailInputStep;
import org.example.steps.impl.volunteer.ExperienceInputStep;
import org.example.steps.impl.volunteer.FullNameInputStep;
import org.example.steps.impl.volunteer.GenderChoiceStep;
import org.example.steps.impl.volunteer.PhoneInputStep;
import org.example.steps.impl.volunteer.PhotoSendStep;
import org.example.steps.impl.volunteer.ReasonInputStep;
import org.example.steps.impl.volunteer.VkInputStep;
import org.example.steps.impl.volunteer.VolunteerIdInputStep;
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
        conversationMap.put(EConversation.VOLUNTEER_REGISTER, volunteerRegisterConversationStepGraph());
        conversationMap.put(EConversation.PARENT_REGISTER, parentRegisterConversationStepGraph());
        return conversationMap;
    }

    @Bean
    public Map<EConversation, EConversationStep> conversationStartStepMap() {
        Map<EConversation, EConversationStep> conversationStartStepMap = new HashMap<>();
        conversationStartStepMap.put(EConversation.VOLUNTEER_REGISTER, EConversationStep.CITY_CHOICE);
        conversationStartStepMap.put(EConversation.PARENT_REGISTER, EConversationStep.PARENT_FULL_NAME_INPUT);
        return conversationStartStepMap;
    }

    @Bean
    public Map<EConversationStep, ConversationStep> conversationStepMap(
            @Autowired CityChoiceStep cityChoiceStep,
            @Autowired CityInputStep cityInputStep,
            @Autowired BirthdayInputStep birthdayInputStep,
            @Autowired ChildFileSendStep childDocumentSendStep,
            @Autowired FullNameInputStep fullNameInputStep,
            @Autowired GenderChoiceStep genderChoiceStep,
            @Autowired PhoneInputStep phoneInputStep,
            @Autowired EducationStatusChoiceStep educationStatusChoiceStep,
            @Autowired EducationInstitutionChoiceStep educationInstitutionChoiceStep,
            @Autowired EducationInstitutionInputStep educationInstitutionInputStep,
            @Autowired AgreementChoiceStep agreementChoiceStep,
            @Autowired VkInputStep vkInputStep,
            @Autowired ClothingSizeChoiceStep clothingSizeChoiceStep,
            @Autowired ReasonInputStep reasonInputStep,
            @Autowired ExperienceInputStep experienceInputStep,
            @Autowired PhotoSendStep photoSendStep,
            @Autowired EmailInputStep emailInputStep,
            @Autowired VolunteerIdInputStep volunteerIdInputStep,
            @Autowired ChildFullNameInputStep childFullNameInputStep,
            @Autowired ChildBirthdayInputStep childBirthdayInputStep,
            @Autowired ChildRegisterPlaceInputStep childRegisterPlaceInputStep,
            @Autowired ParentFullNameInputStep parentFullNameInputStep,
            @Autowired ParentBirthdayInputStep parentBirthdayInputStep,
            @Autowired ParentRegisterPlaceInputStep parentRegisterPlaceInputStep
    ) {
        return new HashMap<>() {{
            put(EConversationStep.CITY_CHOICE, cityChoiceStep);
            put(EConversationStep.CITY_INPUT, cityInputStep);
            put(EConversationStep.BIRTHDAY_INPUT, birthdayInputStep);
            put(EConversationStep.CHILD_DOCUMENT_SEND, childDocumentSendStep);
            put(EConversationStep.FULL_NAME_INPUT, fullNameInputStep);
            put(EConversationStep.GENDER_CHOICE, genderChoiceStep);
            put(EConversationStep.PHONE_INPUT, phoneInputStep);
            put(EConversationStep.EDUCATION_STATUS_CHOICE, educationStatusChoiceStep);
            put(EConversationStep.EDUCATION_INSTITUTION_CHOICE, educationInstitutionChoiceStep);
            put(EConversationStep.EDUCATION_INSTITUTION_INPUT, educationInstitutionInputStep);
            put(EConversationStep.AGREEMENT_CHOICE, agreementChoiceStep);
            put(EConversationStep.VK_INPUT, vkInputStep);
            put(EConversationStep.CLOTHING_SIZE_CHOICE, clothingSizeChoiceStep);
            put(EConversationStep.REASON_INPUT, reasonInputStep);
            put(EConversationStep.EXPERIENCE_INPUT, experienceInputStep);
            put(EConversationStep.PHOTO_SEND, photoSendStep);
            put(EConversationStep.EMAIL_INPUT, emailInputStep);
            put(EConversationStep.VOLUNTEER_ID_INPUT, volunteerIdInputStep);
            put(EConversationStep.CHILD_FULL_NAME_INPUT, childFullNameInputStep);
            put(EConversationStep.CHILD_BIRTHDAY_INPUT, childBirthdayInputStep);
            put(EConversationStep.CHILD_REGISTER_PLACE, childRegisterPlaceInputStep);
            put(EConversationStep.PARENT_FULL_NAME_INPUT, parentFullNameInputStep);
            put(EConversationStep.PARENT_BIRTHDAY_INPUT, parentBirthdayInputStep);
            put(EConversationStep.PARENT_REGISTER_PLACE, parentRegisterPlaceInputStep);
        }};
    }

    private Map<EConversationStep, List<EConversationStep>> volunteerRegisterConversationStepGraph() {
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

    private Map<EConversationStep, List<EConversationStep>> parentRegisterConversationStepGraph() {
        return new HashMap<>() {{
            put(EConversationStep.PARENT_FULL_NAME_INPUT, new ArrayList<>() {{
                add(EConversationStep.PARENT_BIRTHDAY_INPUT);
            }});
            put(EConversationStep.PARENT_BIRTHDAY_INPUT, new ArrayList<>() {{
                add(EConversationStep.PARENT_REGISTER_PLACE);
            }});
            put(EConversationStep.PARENT_REGISTER_PLACE, new ArrayList<>() {{
                add(EConversationStep.CHILD_FULL_NAME_INPUT);
            }});
            put(EConversationStep.CHILD_FULL_NAME_INPUT, new ArrayList<>() {{
                add(EConversationStep.CHILD_BIRTHDAY_INPUT);
            }});
            put(EConversationStep.CHILD_BIRTHDAY_INPUT, new ArrayList<>() {{
                add(EConversationStep.CHILD_REGISTER_PLACE);
            }});
            put(EConversationStep.CHILD_REGISTER_PLACE, new ArrayList<>() {{
                add(null);
            }});
        }};
    }
}
