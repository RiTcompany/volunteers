package org.example.configs;

import org.example.conversation.AConversation;
import org.example.conversation.impl.ChangeChildDocumentConversation;
import org.example.conversation.impl.ChangeVolunteerPhotoConversation;
import org.example.conversation.impl.CheckChildDocumentConversation;
import org.example.conversation.impl.CheckVolunteerPhotoConversation;
import org.example.conversation.impl.ParentRegistrationConversation;
import org.example.conversation.impl.SendBotMessageConversation;
import org.example.conversation.impl.VolunteerRegistrationConversation;
import org.example.enums.EConversation;
import org.example.enums.EConversationStep;
import org.example.steps.ConversationStep;
import org.example.steps.impl.moderator.impl.ChildDocumentCheckChoiceStep;
import org.example.steps.impl.moderator.impl.ChildDocumentFailMessageInputStep;
import org.example.steps.impl.moderator.impl.VolunteerPhotoCheckChoiceStep;
import org.example.steps.impl.moderator.impl.VolunteerPhotoFailMessageInputStep;
import org.example.steps.impl.parent.ChildBirthdayInputStep;
import org.example.steps.impl.parent.ChildFullNameInputStep;
import org.example.steps.impl.parent.ChildRegisterPlaceInputStep;
import org.example.steps.impl.parent.ParentBirthdayInputStep;
import org.example.steps.impl.parent.ParentFullNameInputStep;
import org.example.steps.impl.parent.ParentRegisterPlaceInputStep;
import org.example.steps.impl.volunteer.AgreementChoiceStep;
import org.example.steps.impl.volunteer.AnorakExistChoiceStep;
import org.example.steps.impl.volunteer.AnorakTypeChoiceStep;
import org.example.steps.impl.volunteer.BirthdayInputStep;
import org.example.steps.impl.volunteer.ChildDocumentSendStep;
import org.example.steps.impl.volunteer.CityChoiceStep;
import org.example.steps.impl.volunteer.CityInputStep;
import org.example.steps.impl.volunteer.ClothingSizeChoiceStep;
import org.example.steps.impl.volunteer.EducationInstitutionChoiceStep;
import org.example.steps.impl.volunteer.EducationInstitutionInputStep;
import org.example.steps.impl.volunteer.EducationStatusChoiceStep;
import org.example.steps.impl.volunteer.EducationalSpecialtyInputStep;
import org.example.steps.impl.volunteer.EmailInputStep;
import org.example.steps.impl.volunteer.ExperienceInputStep;
import org.example.steps.impl.volunteer.FullNameInputStep;
import org.example.steps.impl.volunteer.GenderChoiceStep;
import org.example.steps.impl.volunteer.PhoneInputStep;
import org.example.steps.impl.volunteer.PhotoSendStep;
import org.example.steps.impl.volunteer.ReasonInputStep;
import org.example.steps.impl.volunteer.SpbDistrictChoiceStep;
import org.example.steps.impl.volunteer.SweatshirtExistChoiceStep;
import org.example.steps.impl.volunteer.TShirtExistChoiceStep;
import org.example.steps.impl.volunteer.VkInputStep;
import org.example.steps.impl.volunteer.VolunteerIdInputStep;
import org.example.steps.impl.writer.ButtonAddChoiceStep;
import org.example.steps.impl.writer.ButtonInputStep;
import org.example.steps.impl.writer.SendBotMessageChoiceStep;
import org.example.steps.impl.writer.TextInputStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ConversationConfig {
    @Bean
    public Map<EConversation, AConversation> conversationMap(
            @Autowired VolunteerRegistrationConversation volunteerRegistrationConversation,
            @Autowired ParentRegistrationConversation parentRegistrationConversation,
            @Autowired CheckChildDocumentConversation checkChildDocumentConversation,
            @Autowired CheckVolunteerPhotoConversation checkVolunteerPhotoConversation,
            @Autowired ChangeChildDocumentConversation changeChildDocumentConversation,
            @Autowired ChangeVolunteerPhotoConversation changeVolunteerPhotoConversation,
            @Autowired SendBotMessageConversation sendBotMessageConversation
    ) {
        Map<EConversation, AConversation> conversationMap = new HashMap<>();
        conversationMap.put(EConversation.VOLUNTEER_REGISTER, volunteerRegistrationConversation);
        conversationMap.put(EConversation.PARENT_REGISTER, parentRegistrationConversation);
        conversationMap.put(EConversation.CHECK_CHILD_DOCUMENT, checkChildDocumentConversation);
        conversationMap.put(EConversation.CHECK_VOLUNTEER_PHOTO, checkVolunteerPhotoConversation);
        conversationMap.put(EConversation.CHANGE_CHILD_DOCUMENT, changeChildDocumentConversation);
        conversationMap.put(EConversation.CHANGE_VOLUNTEER_PHOTO, changeVolunteerPhotoConversation);
        conversationMap.put(EConversation.SEND_BOT_MESSAGE, sendBotMessageConversation);
        return conversationMap;
    }

    @Bean
    public Map<EConversationStep, ConversationStep> conversationStepMap(
            @Autowired CityChoiceStep cityChoiceStep,
            @Autowired CityInputStep cityInputStep,
            @Autowired BirthdayInputStep birthdayInputStep,
            @Autowired ChildDocumentSendStep childDocumentSendStep,
            @Autowired FullNameInputStep fullNameInputStep,
            @Autowired GenderChoiceStep genderChoiceStep,
            @Autowired PhoneInputStep phoneInputStep,
            @Autowired EducationStatusChoiceStep educationStatusChoiceStep,
            @Autowired EducationInstitutionChoiceStep educationInstitutionChoiceStep,
            @Autowired EducationInstitutionInputStep educationInstitutionInputStep,
            @Autowired EducationalSpecialtyInputStep educationalSpecialtyInputStep,
            @Autowired SpbDistrictChoiceStep spbDistrictChoiceStep,
            @Autowired AgreementChoiceStep agreementChoiceStep,
            @Autowired VkInputStep vkInputStep,
            @Autowired ClothingSizeChoiceStep clothingSizeChoiceStep,
            @Autowired AnorakExistChoiceStep anorakExistChoiceStep,
            @Autowired AnorakTypeChoiceStep anorakTypeChoiceStep,
            @Autowired SweatshirtExistChoiceStep sweatshirtExistChoiceStep,
            @Autowired TShirtExistChoiceStep tShirtExistChoiceStep,
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
            @Autowired ParentRegisterPlaceInputStep parentRegisterPlaceInputStep,
            @Autowired ChildDocumentCheckChoiceStep childDocumentCheckChoiceStep,
            @Autowired ChildDocumentFailMessageInputStep childDocumentFailMessageInputStep,
            @Autowired VolunteerPhotoCheckChoiceStep volunteerPhotoCheckChoiceStep,
            @Autowired VolunteerPhotoFailMessageInputStep volunteerPhotoFailMessageInputStep,
            @Autowired TextInputStep textInputStep,
            @Autowired ButtonAddChoiceStep buttonAddChoiceStep,
            @Autowired ButtonInputStep buttonInputStep,
            @Autowired SendBotMessageChoiceStep sendBotMessageChoiceStep
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
            put(EConversationStep.EDUCATION_SPECIALITY_INPUT, educationalSpecialtyInputStep);
            put(EConversationStep.SPB_DISTRICT_CHOICE, spbDistrictChoiceStep);
            put(EConversationStep.AGREEMENT_CHOICE, agreementChoiceStep);
            put(EConversationStep.VK_INPUT, vkInputStep);
            put(EConversationStep.CLOTHING_SIZE_CHOICE, clothingSizeChoiceStep);
            put(EConversationStep.ANORAK_EXIST_CHOICE, anorakExistChoiceStep);
            put(EConversationStep.ANORAK_TYPE_CHOICE, anorakTypeChoiceStep);
            put(EConversationStep.SWEATSHIRT_EXIST_CHOICE, sweatshirtExistChoiceStep);
            put(EConversationStep.T_SHIRT_EXIST_CHOICE, tShirtExistChoiceStep);
            put(EConversationStep.REASON_INPUT, reasonInputStep);
            put(EConversationStep.EXPERIENCE_INPUT, experienceInputStep);
            put(EConversationStep.PHOTO_SEND, photoSendStep);
            put(EConversationStep.EMAIL_INPUT, emailInputStep);
            put(EConversationStep.VOLUNTEER_ID_INPUT, volunteerIdInputStep);
            put(EConversationStep.CHILD_FULL_NAME_INPUT, childFullNameInputStep);
            put(EConversationStep.CHILD_BIRTHDAY_INPUT, childBirthdayInputStep);
            put(EConversationStep.CHILD_REGISTER_PLACE_INPUT, childRegisterPlaceInputStep);
            put(EConversationStep.BOT_MESSAGE_TEXT_INPUT, textInputStep);
            put(EConversationStep.BOT_MESSAGE_BUTTON_ADD_CHOICE, buttonAddChoiceStep);
            put(EConversationStep.BOT_MESSAGE_BUTTON_INPUT, buttonInputStep);
            put(EConversationStep.SEND_BOT_MESSAGE_CHOICE, sendBotMessageChoiceStep);
            put(EConversationStep.PARENT_FULL_NAME_INPUT, parentFullNameInputStep);
            put(EConversationStep.PARENT_BIRTHDAY_INPUT, parentBirthdayInputStep);
            put(EConversationStep.PARENT_REGISTER_PLACE_INPUT, parentRegisterPlaceInputStep);
            put(EConversationStep.CHILD_DOCUMENT_CHECK_CHOICE, childDocumentCheckChoiceStep);
            put(EConversationStep.CHILD_DOCUMENT_FAIL_MESSAGE_INPUT, childDocumentFailMessageInputStep);
            put(EConversationStep.VOLUNTEER_PHOTO_CHECK_CHOICE, volunteerPhotoCheckChoiceStep);
            put(EConversationStep.VOLUNTEER_PHOTO_FAIL_MESSAGE_INPUT, volunteerPhotoFailMessageInputStep);
        }};
    }
}
