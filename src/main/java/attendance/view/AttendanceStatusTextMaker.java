package attendance.view;

import attendance.model.AttendanceStatusChecker.AttendanceStatus;

public class AttendanceStatusTextMaker {

    public static final String ATTENDANCE_TEXT = "출석";
    public static final String LATE_TEXT = "지각";
    public static final String ABSENT_TEXT = "결석";

    public static String make(final AttendanceStatus attendanceStatus) {
        if (attendanceStatus == AttendanceStatus.ABSENT) {
            return ABSENT_TEXT;
        }
        if (attendanceStatus == AttendanceStatus.LATE) {
            return LATE_TEXT;
        }
        return ATTENDANCE_TEXT;
    }
}
