package attendance.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Attendances {

    private final List<AttendanceDateTime> attendanceDateTimes;

    public Attendances(final List<AttendanceDateTime> attendanceDateTimes) {
        this.attendanceDateTimes = attendanceDateTimes;
    }

    public boolean isSameDateExists(final AttendanceDateTime dateTime) {
        return this.attendanceDateTimes.stream()
                .anyMatch(attendanceDateTime -> attendanceDateTime.isSameDate(dateTime));
    }

    public void addAttendanceDateTime(final AttendanceDateTime attendanceDateTime) {
        this.attendanceDateTimes.add(attendanceDateTime);
    }

    public AttendanceDateTime findByLocalDate(final LocalDate findDate) {
        return attendanceDateTimes.stream()
                .filter(attendanceDateTime -> attendanceDateTime.isSameDate(findDate))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 일자에 출석하지 않았습니다."));
    }

    public void removeAttendanceDateTime(final AttendanceDateTime dateTime) {
        this.attendanceDateTimes.remove(dateTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attendances that = (Attendances) o;
        return Objects.equals(attendanceDateTimes, that.attendanceDateTimes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attendanceDateTimes);
    }
}
