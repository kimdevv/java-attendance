package attendance.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public class Crews {

    private final List<Crew> crews;

    public Crews(final List<Crew> crews) {
        this.crews = crews;
    }

    private Crew findByNickname(final String nickname) {
        return crews.stream()
                .filter(crew -> crew.getNickname().equalsIgnoreCase(nickname))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 크루의 닉네임입니다."));
    }

    public void validateCanAttendToday(final String nickname) {
        Crew crew = findByNickname(nickname);
        if (crew.isAttendedToday()) {
            throw new IllegalArgumentException("오늘은 이미 출석하셨습니다. 출석 수정 기능을 이용해 주세요.");
        }
    }

    public void addCrewAttendance(final String nickname, final LocalDate today, final LocalTime attendanceTime) {
        Crew crew = findByNickname(nickname);
        crew.addAttendance(today, attendanceTime);
    }

    public LocalTime findCrewAttendanceTimeAt(final String nickname, final LocalDate attendanceDate) {
        Crew crew = findByNickname(nickname);
        return crew.findAttendanceTimeAt(attendanceDate);
    }

    public void modifyCrewAttendanceTime(final String nickname, final LocalDate attendanceDate, final LocalTime newAttendanceTime) {
        Crew crew = findByNickname(nickname);
        crew.modifyAttendanceTime(attendanceDate, newAttendanceTime);
    }

    public Map<LocalDate, LocalTime> getCrewAttendances(final String nickname) {
        Crew crew = findByNickname(nickname);
        return crew.getAttendances();
    }

    public Map<AttendanceStatusChecker.AttendanceStatus, Long> calculateCrewAttendanceStatuses(final String nickname) {
        Crew crew = findByNickname(nickname);
        return crew.calculateAttendanceStatuses();
    }

    public ExpulsionStatus calculateCrewExpulsionStatus(final String nickname) {
        Crew crew = findByNickname(nickname);
        return crew.calculateExpulsionStatus();
    }

    public List<Crew> findExpulsionCrews() {
        return crews.stream()
                .filter(crew -> crew.calculateExpulsionStatus() != ExpulsionStatus.NONE)
                .toList();
    }
}
