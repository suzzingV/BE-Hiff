package hiff.hiff.behiff.global.util;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateCalculator {

    public static int calculateAge(LocalDate birthdate) {
        LocalDate today = LocalDate.now();
        return Period.between(birthdate, today).getYears();
    }

    public static String getTodayDate() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMdd");
        return today.format(formatter);
    }

    public static String getTomorrowDate() {
        LocalDate today = LocalDate.now().plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMdd");
        return today.format(formatter);
    }
}
