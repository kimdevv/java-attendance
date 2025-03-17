package attendance.model;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AttendanceStatusChecker {

    public static final LocalTime TUESDAY_TO_FRIDAY_ACCEPTABLE_ATTENDANCE_TIME = LocalTime.of(10, 0);
    public static final LocalTime MONDAY_ACCEPTABLE_ATTENDANCE_TIME = LocalTime.of(13, 0);
    public static final int ATTENDANCE_DEADLINE_MINUTE = 5;
    public static final int LATE_DEADLINE_MINUTE = 30;
    public static final int LATE_COUNT_PER_ABSENT = 3;


    public enum AttendanceStatus {
        ATTENDANCE,
        LATE,
        ABSENT;
    }

    public static AttendanceStatus checkStatus(final AttendanceDateTime attendanceDateTime) {
        if (attendanceDateTime.getLocalDateTime().toLocalTime().equals(AttendanceDateTime.ABSENT_TIME)) {
            return AttendanceStatus.ABSENT;
        }
        if (attendanceDateTime.calculateDayOfWeek().equals(DayOfWeek.MONDAY)) {
            int minuteDifference = attendanceDateTime.calculateMinuteDifference(MONDAY_ACCEPTABLE_ATTENDANCE_TIME);
            return findAttendanceStatusByMinuteDifference(minuteDifference);
        }
        int minuteDifference = attendanceDateTime.calculateMinuteDifference(TUESDAY_TO_FRIDAY_ACCEPTABLE_ATTENDANCE_TIME);
        return findAttendanceStatusByMinuteDifference(minuteDifference);
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

    public static Map<AttendanceStatus, Long> checkStatuses(final List<AttendanceDateTime> attendanceDateTimes) {
        Map<AttendanceStatus, Long> attendanceStatuses = Arrays.stream(AttendanceStatus.values())
                .collect(Collectors.toMap(status -> status, status -> 0L));
        attendanceDateTimes.stream()
                .map(AttendanceStatusChecker::checkStatus)
                .collect(Collectors.groupingBy(attendanceStatus -> attendanceStatus, Collectors.counting()))
                .forEach(attendanceStatuses::put);
        return attendanceStatuses;
    }

    public static long calculateAllAbsent(final List<AttendanceDateTime> attendanceDateTimes) {
        Map<AttendanceStatus, Long> attendanceStatuses = checkStatuses(attendanceDateTimes);
        long absentCount = attendanceStatuses.get(AttendanceStatus.ABSENT);
        absentCount += attendanceStatuses.get(AttendanceStatus.LATE) / LATE_COUNT_PER_ABSENT;
        return absentCount;
    }
}
