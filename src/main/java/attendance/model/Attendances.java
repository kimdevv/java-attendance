package attendance.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class Attendances {

    public static final LocalTime ABSENT_TIME = LocalTime.of(23, 59, 59);
    public static final int START_DAY_OF_MONTH = 1;
    public static final int LATE_COUNT_PER_ABSENT = 3;
    private final Map<LocalDate, LocalTime> attendances;

    public Attendances(final Map<LocalDate, LocalTime> attendances) {
        this.attendances = attendances;
    }

    public static Attendances generateMonthAttendancesUntilToday() {
        Map<LocalDate, LocalTime> attendances = new LinkedHashMap<>();
        for (int i=START_DAY_OF_MONTH; i<=LocalDate.now().getDayOfMonth(); i++) {
            LocalDate date = LocalDate.now().withDayOfMonth(i);
            putDateIfNotHolidayOrWeekend(date, attendances, i);
        }
        return new Attendances(attendances);
    }

    private static void putDateIfNotHolidayOrWeekend(final LocalDate date, final Map<LocalDate, LocalTime> attendances, int i) {
        if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY || Holiday.isHoliday(date)) {
            return;
        }
        attendances.put(LocalDate.now().withDayOfMonth(i), ABSENT_TIME);
    }

    public void addAttendance(final LocalDate attendanceDate, final LocalTime attendanceTime) {
        attendances.put(attendanceDate, attendanceTime);
    }

    public boolean isAttendedAt(final LocalDate date) {
        return attendances.get(date) != ABSENT_TIME;
    }

    public LocalTime findAttendanceTimeAtGivenDate(final LocalDate attendanceDate) {
        if (attendances.containsKey(attendanceDate)) {
            return attendances.get(attendanceDate);
        }
        throw new IllegalArgumentException("해당 날짜에 출석하지 않았습니다.");
    }

    public void modifyAttendanceTime(final LocalDate attendanceDate, final LocalTime newAttendanceTime) {
        attendances.put(attendanceDate, newAttendanceTime);
    }

    public Map<AttendanceStatusChecker.AttendanceStatus, Long> calculateAttendanceStatuses() {
        return AttendanceStatusChecker.checkStatuses(attendances);
    }

    public ExpulsionStatus calculateExpulsionStatus() {
        Map<AttendanceStatusChecker.AttendanceStatus, Long> attendanceStatuses = calculateAttendanceStatuses();
        return ExpulsionStatus.from(calculateTotalAbsentCounts(attendanceStatuses));
    }

    public int calculateTotalAbsentCounts(final Map<AttendanceStatusChecker.AttendanceStatus, Long> attendanceStatuses) {
        long absentCount = attendanceStatuses.get(AttendanceStatusChecker.AttendanceStatus.ABSENT);
        absentCount += attendanceStatuses.get(AttendanceStatusChecker.AttendanceStatus.LATE) / LATE_COUNT_PER_ABSENT;
        return Math.toIntExact(absentCount);
    }

    public Map<LocalDate, LocalTime> getAttendances() {
        return attendances;
    }
}
