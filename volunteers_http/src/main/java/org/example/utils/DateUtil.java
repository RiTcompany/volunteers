package org.example.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    public static String birthdayWithAge(Date birthdayDate) {
        return dateFormat.format(birthdayDate).concat(" ").concat(Integer.toString(age(birthdayDate)));
    }

    public static int age(Date birthdayDate) {
        Calendar today = Calendar.getInstance();
        Calendar birthday = Calendar.getInstance();
        birthday.setTime(birthdayDate);

        return today.get(Calendar.YEAR) - birthday.get(Calendar.YEAR);
    }
}
