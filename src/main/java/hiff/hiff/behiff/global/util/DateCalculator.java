package hiff.hiff.behiff.global.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateCalculator {

    public static String TODAY_DATE = "";

    public static int calculateAge(LocalDate birthdate) {
        LocalDate today = LocalDate.now();
        return Period.between(birthdate, today).getYears();
    }

    public static String getMatchingDate() {
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
}
