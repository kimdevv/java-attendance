package attendance.model;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class AttendanceStatusChecker {

    public static final LocalTime TUESDAY_TO_FRIDAY_ACCEPTABLE_ATTENDANCE_TIME = LocalTime.of(10, 0);
    public static final LocalTime MONDAY_ACCEPTABLE_ATTENDANCE_TIME = LocalTime.of(13, 0);
    public static final int ATTENDANCE_DEADLINE_MINUTE = 5;
    public static final int LATE_DEADLINE_MINUTE = 30;

    public enum AttendanceStatus {
        ATTENDANCE,
        LATE,
        ABSENT;
    }

    public static AttendanceStatus checkStatus(final LocalDate attendanceDate, final LocalTime attendanceTime) {
        if (attendanceTime.equals(Attendances.ABSENT_TIME)) {
            return AttendanceStatus.ABSENT;
        }
        if (attendanceDate.getDayOfWeek().equals(DayOfWeek.MONDAY)) {
            int minuteDifference = calculateMinuteDifference(attendanceTime, MONDAY_ACCEPTABLE_ATTENDANCE_TIME);
            return findAttendanceStatusByMinuteDifference(minuteDifference);
        }
        int minuteDifference = calculateMinuteDifference(attendanceTime, TUESDAY_TO_FRIDAY_ACCEPTABLE_ATTENDANCE_TIME);
        return findAttendanceStatusByMinuteDifference(minuteDifference);
    }

    private static int calculateMinuteDifference(final LocalTime firstTime, final LocalTime secondTime) {
        return (int) Duration.between(secondTime, firstTime)
                .toMinutes();
    }

    private static AttendanceStatus findAttendanceStatusByMinuteDifference(final int minuteDifference) {
        if (minuteDifference > LATE_DEADLINE_MINUTE) {
            return AttendanceStatus.ABSENT;
        }
        if (minuteDifference > ATTENDANCE_DEADLINE_MINUTE) {
            return AttendanceStatus.LATE;
        }
        return AttendanceStatus.ATTENDANCE;
    }

    public static Map<AttendanceStatus, Long> checkStatuses(final Map<LocalDate, LocalTime> attendances) {
        Map<AttendanceStatus, Long> attendanceStatuses = Arrays.stream(AttendanceStatus.values())
                .collect(Collectors.toMap(status -> status, status -> 0L));
        attendanceStatuses.putAll(attendances.keySet().stream()
                .map(attendanceDate -> checkStatus(attendanceDate, attendances.get(attendanceDate)))
                .collect(Collectors.groupingBy(attendanceStatus -> attendanceStatus, Collectors.counting())));
        return attendanceStatuses;
    }
}
