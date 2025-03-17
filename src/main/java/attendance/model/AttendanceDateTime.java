package attendance.model;

import java.time.*;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Objects;

public class AttendanceDateTime {

    public static final LocalTime CAMPUS_OPEN_TIME = LocalTime.of(8, 0);
    public static final LocalTime CAMPUS_CLOSE_TIME = LocalTime.of(23, 0);
    public static final LocalTime ABSENT_TIME = LocalTime.of(0, 0);
    private final LocalDateTime dateTime;

    private AttendanceDateTime(final LocalDate absentDate) {
        LocalDateTime absentDateTime = LocalDateTime.of(absentDate, ABSENT_TIME);
        this.dateTime = absentDateTime;
    }

    public AttendanceDateTime(final LocalDateTime attendanceDateTime) {
        validate(attendanceDateTime);
        this.dateTime = attendanceDateTime;
    }

    private void validate(final LocalDateTime attendanceDateTime) {
        validateFuture(attendanceDateTime);
        validateDate(attendanceDateTime);
        validateTime(attendanceDateTime);
    }

    private void validateFuture(final LocalDateTime attendanceDateTime) {
        if (attendanceDateTime.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("미래의 시간으로는 출석할 수 없습니다.");
        }
    }

    private void validateDate(final LocalDateTime attendanceDateTime) {
        if (isWeekend(attendanceDateTime) || Holiday.isHoliday(attendanceDateTime)) {
            throw new IllegalArgumentException("%d월 %d일 %s은 등교일이 아닙니다.".formatted(
                    attendanceDateTime.getMonthValue(),
                    attendanceDateTime.getDayOfMonth(),
                    attendanceDateTime.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREA)
            ));
        }
    }

    public static boolean isWeekend(final LocalDateTime dateTime) {
        DayOfWeek dayOfWeek = dateTime.getDayOfWeek();
        if (dayOfWeek.equals(DayOfWeek.SATURDAY) || dayOfWeek.equals(DayOfWeek.SUNDAY)) {
            return true;
        }
        return false;
    }

    private void validateTime(final LocalDateTime attendanceDateTime) {
        LocalTime attendanceTime = attendanceDateTime.toLocalTime();
        if (attendanceTime.isBefore(CAMPUS_OPEN_TIME) || attendanceTime.isAfter(CAMPUS_CLOSE_TIME)) {
            throw new IllegalArgumentException("%d시 %d분은 캠퍼스 운영 시간이 아닙니다.".formatted(
                    attendanceTime.getHour(),
                    attendanceTime.getMinute()
            ));
        }
    }

    public DayOfWeek calculateDayOfWeek() {
        return this.dateTime.getDayOfWeek();
    }

    public int calculateMinuteDifference(final LocalTime otherTime) {
        return (int)Duration.between(otherTime, dateTime)
                .toMinutes();
    }

    public boolean isSameDate(final AttendanceDateTime otherDateTime) {
        return isSameDate(otherDateTime.getLocalDateTime().toLocalDate());
    }

    public boolean isSameDate(final LocalDate date) {
        return this.dateTime.toLocalDate().equals(date);
    }

    public AttendanceDateTime changeTime(final LocalTime newTime) {
        LocalDateTime newDateTime = LocalDateTime.of(this.dateTime.toLocalDate(), newTime);
        return new AttendanceDateTime(newDateTime);
    }

    public static AttendanceDateTime createAbsentDateTime(final LocalDate absentDate) {
        return new AttendanceDateTime(absentDate);
    }

    public LocalDateTime getLocalDateTime() {
        return dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttendanceDateTime that = (AttendanceDateTime) o;
        return Objects.equals(dateTime, that.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateTime);
    }
}
