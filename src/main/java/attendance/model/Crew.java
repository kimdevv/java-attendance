package attendance.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Crew {

    public static final int MINIMUM_NICKNAME_LENGTH = 2;
    public static final int MAXIMUM_NICKNAME_LENGTH = 5;

    private final String nickname;
    private final Attendances attendances;

    private Crew(final String nickname, final Attendances attendances) {
        validateLength(nickname);
        this.nickname = nickname;
        this.attendances = attendances;
    }

    public static Crew generate(final String nickname) {
        return new Crew(nickname, new Attendances(new HashMap<>()));
    }

    public static Crew generateWithAttendancesUntilToday(final String nickname) {
        return new Crew(nickname, Attendances.generateMonthAttendancesUntilToday());
    }

    private void validateLength(final String nickname) {
        final int nicknameLength = nickname.replace(" ", "").length();
        if (!(MINIMUM_NICKNAME_LENGTH <= nicknameLength && nicknameLength <= MAXIMUM_NICKNAME_LENGTH)) {
            throw new IllegalArgumentException("크루의 닉네임은 공백 제외 2글자 이상, 5글자 이하로 입력해 주세요.");
        }
    }

    public void addAttendance(final LocalDateTime attendanceDateTime) {
        addAttendance(attendanceDateTime.toLocalDate(), attendanceDateTime.toLocalTime());
    }

    public void addAttendance(final LocalDate attendanceDate, final LocalTime attendanceTime) {
        attendances.addAttendance(attendanceDate, attendanceTime);
    }

    public boolean isAttendedToday() {
        return isAttendedAt(LocalDate.now());
    }

    public boolean isAttendedAt(final LocalDate date) {
        return attendances.isAttendedAt(date);
    }

    public LocalTime findAttendanceTimeAt(final LocalDate attendanceDate) {
        return attendances.findAttendanceTimeAt(attendanceDate);
    }

    public void modifyAttendanceTime(final LocalDate attendanceDate, final LocalTime newAttendanceTime) {
        attendances.modifyAttendanceTime(attendanceDate, newAttendanceTime);
    }

    public Map<AttendanceStatusChecker.AttendanceStatus, Long> calculateAttendanceStatuses() {
        return attendances.calculateAttendanceStatuses();
    }

    public ExpulsionStatus calculateExpulsionStatus() {
        return attendances.calculateExpulsionStatus();
    }

    public int calculateTotalAbsentCounts() {
        Map<AttendanceStatusChecker.AttendanceStatus, Long> attendanceStatuses = attendances.calculateAttendanceStatuses();
        return attendances.calculateTotalAbsentCounts(attendanceStatuses);
    }

    public String getNickname() {
        return nickname;
    }

    public Map<LocalDate, LocalTime> getAttendances() {
        return attendances.getAttendances();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Crew crew = (Crew) o;
        return Objects.equals(nickname, crew.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickname);
    }
}
