package attendance.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.IntStream;

public class AttendanceBook {

    public static final int START_DAY_OF_MONTH = 1;
    private final Map<Crew, Attendances> crewAttedances;

    public AttendanceBook(final Map<Crew, Attendances> crewAttendances) {
        this.crewAttedances = crewAttendances;
    }

    public void validateRegisteredCrew(final Crew crew) {
        if (!this.crewAttedances.containsKey(crew)) {
            throw new IllegalArgumentException("등록되지 않은 닉네임입니다.");
        }
    }

    public void saveAttendanceDateTime(final Crew crew, final AttendanceDateTime attendanceDateTime) {
        validateDuplicateAttendanceDate(crew, attendanceDateTime);
        Attendances attendances = this.crewAttedances.get(crew);
        attendances.addAttendanceDateTime(attendanceDateTime);
    }

    private void validateDuplicateAttendanceDate(final Crew crew, final AttendanceDateTime attendanceDateTime) {
        Attendances attendances = this.crewAttedances.get(crew);
        if (attendances.isSameDateExists(attendanceDateTime)) {
            throw new IllegalArgumentException("오늘은 이미 출석하셨습니다. 출석 수정 기능을 이용해 주세요.");
        }
    }

    public AttendanceDateTime changeCrewAttendanceTime(final Crew crew, final AttendanceDateTime originalDateTime, final LocalTime newTime) {
        removeAttendanceDateTime(crew, originalDateTime);
        AttendanceDateTime newDateTime = originalDateTime.changeTime(newTime);
        saveAttendanceDateTime(crew, newDateTime);
        return newDateTime;
    }

    public AttendanceDateTime findAttendanceDateTimeByCrewAndDate(final Crew crew, final LocalDate findDate) {
        Attendances attendances = this.crewAttedances.get(crew);
        return attendances.findByLocalDate(findDate);
    }

    private void removeAttendanceDateTime(final Crew crew, final AttendanceDateTime attendanceDateTime) {
        Attendances attendances = this.crewAttedances.get(crew);
        attendances.removeAttendanceDateTime(attendanceDateTime);
    }

    public List<AttendanceDateTime> findCrewAttendancesThisMonth(final Crew crew) {
        final int dayOfToday = LocalDate.now().getDayOfMonth();
        return IntStream.range(START_DAY_OF_MONTH, dayOfToday)
                .mapToObj(day -> LocalDateTime.now().withDayOfMonth(day))
                .filter(dateTime -> !AttendanceDateTime.isWeekend(dateTime) && !Holiday.isHoliday(dateTime))
                .map(dateTime -> findAttendanceDateTimeByCrewAndDateOrAbsent(crew, dateTime.toLocalDate()))
                .toList();
    }

    private AttendanceDateTime findAttendanceDateTimeByCrewAndDateOrAbsent(final Crew crew, final LocalDate findDate) {
        try {
            return findAttendanceDateTimeByCrewAndDate(crew, findDate);
        } catch (IllegalArgumentException exception) {
            return AttendanceDateTime.createAbsentDateTime(findDate);
        }
    }

    public Set<Crew> getAllCrews() {
        return this.crewAttedances.keySet();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttendanceBook that = (AttendanceBook) o;
        return Objects.equals(crewAttedances, that.crewAttedances);
    }

    @Override
    public int hashCode() {
        return Objects.hash(crewAttedances);
    }
}
