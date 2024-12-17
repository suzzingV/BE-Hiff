package hiff.hiff.behiff.global.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateCalculator {

    public static String TODAY_DATE = "";

    public static int calculateAge(LocalDate birthdate) {
        LocalDate today = LocalDate.now();
        return Period.between(birthdate, today).getYears();
    }

    public static String getMatchingDateTimeByString() {
        LocalDateTime today = LocalDateTime.now();

        if(today.getHour() >= 0 && today.getHour() < 3) {
            return TODAY_DATE + "03";
        } else if(today.getHour() >= 3 && today.getHour() < 6) {
            return TODAY_DATE + "06";
        } else if(today.getHour() >= 6 && today.getHour() < 9) {
            return TODAY_DATE + "09";
        } else if(today.getHour() >= 9 && today.getHour() < 12) {
            return TODAY_DATE + "12";
        } else if(today.getHour() >= 12 && today.getHour() < 15) {
            return TODAY_DATE + "15";
        } else if(today.getHour() >= 15 && today.getHour() < 18) {
            return TODAY_DATE + "18";
        } else if(today.getHour() >= 18 && today.getHour() < 21) {
            return TODAY_DATE + "21";
        }
        return getTomorrowDate() + "00";
    }

    public static LocalDate getMatchingDate() {
        LocalDateTime today = LocalDateTime.now();

        if(today.getHour() >= 21) {
            LocalDateTime tomorrow = today.plusDays(1);
            return tomorrow.toLocalDate();
        }
        return today.toLocalDate();
    }

    public static String getYesterdayDate() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMdd");
        return yesterday.format(formatter);
    }

    public static void updateTodayDate() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMdd");
        TODAY_DATE = today.format(formatter);
    }

    public static String getTomorrowDate() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMdd");
        return tomorrow.format(formatter);
    }

    public static String getDateByPattern(LocalDate localDate, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return localDate.format(formatter);
    }
}
