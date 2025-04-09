package attendance.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

public enum Holiday {
    새해(1, 1),
    삼일절(3, 1),
    어린이날(5, 5),
    현충일(6, 6),
    광복절(8, 15),
    개천절(10, 3),
    한글날(10, 9),
    크리스마스(12, 25);

    private final int month;
    private final int day;

    Holiday(final int month, final int day) {
        this.month = month;
        this.day = day;
    }

    public static boolean isHoliday(final LocalDate date) {
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();
        return Arrays.stream(values())
                .anyMatch(holiday -> holiday.month == month && holiday.day == day);
    }
}
