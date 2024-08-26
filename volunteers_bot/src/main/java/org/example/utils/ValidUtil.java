package org.example.utils;

import org.example.dto.MessageDto;
import org.example.dto.ResultDto;
import org.example.enums.EMessage;
import org.example.enums.EYesNo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.regex.Pattern;

public class ValidUtil {
    public static final int MAX_DESCRIPTION_TEXT_LENGTH = 1023;
    public static final int MAX_BOT_MESSAGE_LENGTH = 2047;
    public static final int MAX_BUTTON_TEXT_LENGTH = 20;
    private static final String EXCEPTION_MESSAGE_TEXT = "Слишком длинное сообщение. Постарайтесь уложиться в %d символов";
    private static final int MAX_AGE = 122;
    private static final int MAX_FULL_NAME_LENGTH = 511;
    private static final int SURNAME_INDEX = 0;
    private static final int NAME_INDEX = 1;
    private static final int PATRONYMIC_INDEX = 2;
    private static final int FULL_NAME_PART_NUMBER_WITHOUT_PATRONYMIC = 2;
    private static final int FULL_NAME_PART_NUMBER_WITH_PATRONYMIC = 3;
    private static final int MAX_REGISTER_PLACE_LENGTH = 511;
    private static final Pattern digitPattern = Pattern.compile("^[0-9]+$");
    private static final Pattern letterPattern = Pattern.compile("^[а-яА-ЯёЁa-zA-Z]+$");

    public static boolean isValidURL(String urlString) {
        try {
            new URL(urlString);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }

    public static boolean isLongButtonText(int length) {
        return length >= MAX_BUTTON_TEXT_LENGTH;
    }

    public static String getLongMessageExceptionText(int length) {
        length = length / 100 * 100;
        return EXCEPTION_MESSAGE_TEXT.formatted(length);
    }

    public static boolean isLongBotMessage(String text) {
        return text.length() > MAX_BOT_MESSAGE_LENGTH;
    }

    public static boolean containsDigitsOnly(String data) {
        return digitPattern.matcher(data).matches();
    }

    public static boolean isLongRegisterPlace(String registerPlace) {
        return registerPlace.length() > MAX_REGISTER_PLACE_LENGTH;
    }

    public static ResultDto isValidShortString(String string) {
        if (string.isBlank()) {
            return new ResultDto(false, "Вы ввели пустую строку. Попробуйте снова");
        }

        if (string.length() > MAX_DESCRIPTION_TEXT_LENGTH) {
            String exceptionMessage = ValidUtil.getLongMessageExceptionText(ValidUtil.MAX_DESCRIPTION_TEXT_LENGTH);
            return new ResultDto(false, exceptionMessage);
        }

        return new ResultDto(true);
    }

    public static ResultDto isValidBirthday(String birthdayStr) {
        Date date = DateUtil.convertDate(birthdayStr);
        if (date == null) {
            return new ResultDto(false, "Некорректный формат даты. Введите данные по шаблону <b>дд.мм.гггг</b>");
        }

        if (!isRealBirthday(date)) {
            return new ResultDto(false, "Вы ввели невозможную дату рождения. Введите свои настоящие данные");
        }

        return new ResultDto(true);
    }

    public static ResultDto isValidYesNoChoice(MessageDto messageDto, String exceptionMessage) {
        if (!isCallback(messageDto.getEMessage())) {
            return new ResultDto(false, exceptionMessage);
        }

        try {
            EYesNo.valueOf(messageDto.getData());
            return new ResultDto(true);
        } catch (IllegalArgumentException ignored) {
        }

        return new ResultDto(false, exceptionMessage);
    }

    public static ResultDto isValidFullName(String fullName) {
        if (isLongFullName(fullName)) {
            return new ResultDto(false, "Ваше ФИО слишком длинное, по нашим данным такого существовать не может");
        }

        String[] fullNamePartArray = fullName.split("\\s+");
        if (isIncorrectFullNamePartNumber(fullNamePartArray)) {
            return new ResultDto(false, "Ваше ФИО должно содержать только фамилию из 1 слова, имя из одного слова и отчество из одного слова при наличии");
        }

        if (fullNameContainsIncorrectSymbols(fullNamePartArray[SURNAME_INDEX])) {
            return new ResultDto(false, "Фамилия должна содержать только буквы");
        }

        if (fullNameContainsIncorrectSymbols(fullNamePartArray[NAME_INDEX])) {
            return new ResultDto(false, "Имя должно содержать только буквы");
        }

        if (fullNamePartArray.length == FULL_NAME_PART_NUMBER_WITH_PATRONYMIC
                && fullNameContainsIncorrectSymbols(fullNamePartArray[PATRONYMIC_INDEX])
        ) {
            return new ResultDto(false, "Отчество должно содержать только буквы");
        }

        return new ResultDto(true);
    }

    public static boolean isCallback(EMessage eMessage) {
        return EMessage.CALLBACK.equals(eMessage);
    }

    private static boolean isLongFullName(String fullName) {
        return fullName.length() > MAX_FULL_NAME_LENGTH;
    }

    private static boolean isIncorrectFullNamePartNumber(String[] fullNamePartArray) {
        return fullNamePartArray.length != FULL_NAME_PART_NUMBER_WITHOUT_PATRONYMIC
                && fullNamePartArray.length != FULL_NAME_PART_NUMBER_WITH_PATRONYMIC;
    }

    private static boolean fullNameContainsIncorrectSymbols(String fullNamePart) {
        return !letterPattern.matcher(fullNamePart).matches();
    }

    private static boolean isRealBirthday(Date birthday) {
        int yearCount = DateUtil.getYearCountByDate(birthday);
        return new Date().after(birthday) && yearCount < MAX_AGE;
    }
}
