package attendance.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Crew {

    public static final int MINIMUM_NICKNAME_LENGTH = 2;
    public static final int MAXIMUM_NICKNAME_LENGTH = 5;
    public static final LocalTime ABSENT_TIME = LocalTime.of(23, 59, 59);

    private final String nickname;
    private final Map<LocalDate, LocalTime> attendances;

    private Crew(final String nickname, final Map<LocalDate, LocalTime> attendances) {
        validateLength(nickname);
        this.nickname = nickname;
        this.attendances = attendances;
    }

    public static Crew generate(final String nickname) {
        return new Crew(nickname, new HashMap<>());
    }

    public static Crew generateWithAttendancesUntilToday(final String nickname) {
        Map<LocalDate, LocalTime> attendances = new LinkedHashMap<>();
        for (int i=1; i<=LocalDate.now().getDayOfMonth(); i++) {
            LocalDate date = LocalDate.now().withDayOfMonth(i);
            if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY || Holiday.isHoliday(date)) {
                continue;
            }
            attendances.put(LocalDate.now().withDayOfMonth(i), ABSENT_TIME);
        }
        return new Crew(nickname, attendances);
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
        attendances.put(attendanceDate, attendanceTime);
    }

    public boolean isAttendedToday() {
        return isAttendedAt(LocalDate.now());
    }

    public boolean isAttendedAt(final LocalDate date) {
        if (attendances.get(date) == ABSENT_TIME) {
            return false;
        }
        return true;
    }

    public LocalTime findAttendanceTimeInGivenDate(final LocalDate attendanceDate) {
        if (attendances.containsKey(attendanceDate)) {
            return attendances.get(attendanceDate);
        }
        throw new IllegalArgumentException("해당 날짜에 출석하지 않았습니다.");
    }

    public String getNickname() {
        return nickname;
    }

    public Map<LocalDate, LocalTime> getAttendances() {
        return attendances;
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
