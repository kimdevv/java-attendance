package attendance.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.util.Map;

import static attendance.model.AttendanceStatusChecker.AttendanceStatus.ABSENT;
import static attendance.model.AttendanceStatusChecker.AttendanceStatus.ATTENDANCE;
import static attendance.model.AttendanceStatusChecker.AttendanceStatus.LATE;
import static org.assertj.core.api.Assertions.*;

public class AttendancesTest {

    private Attendances attendances;

    @BeforeEach
    void initialize() {
        attendances = Attendances.generateMonthAttendancesUntilToday();
    }

    @Test
    void 해당_날짜에_출석했는지_검사한다() {
        // Given
        LocalDate today = LocalDate.now();

        // When & Then
        assertThat(attendances.isAttendedAt(today)).isFalse();
    }

    @Test
    void 출석을_등록한다() {
        // Given
        LocalDate attendanceDate = LocalDate.now();
        LocalTime attendanceTime = LocalTime.of(9, 50);

        // When
        attendances.addAttendance(attendanceDate, attendanceTime);

        // Then
        assertThat(attendances.isAttendedAt(attendanceDate)).isTrue();
    }

    @Test
    void 출석한_날짜의_출석_시간을_확인한다() {
        // Given
        LocalDate attendanceDate = LocalDate.now();
        LocalTime attendanceTime = LocalTime.of(10, 5);
        attendances.addAttendance(attendanceDate, attendanceTime);

        // When & Then
        assertThat(attendances.findAttendanceTimeAt(attendanceDate))
                .isEqualTo(LocalTime.of(10, 5));
    }

    @Test
    void 출석한_시간을_수정한다() {
        // Given
        LocalDate attendanceDate = LocalDate.now();
        LocalTime originalAttendanceTime = LocalTime.of(10, 5);
        LocalTime newAttendanceTime = LocalTime.of(10, 15);
        attendances.addAttendance(attendanceDate, originalAttendanceTime);

        // When
        attendances.modifyAttendanceTime(attendanceDate, newAttendanceTime);

        // Then
        assertThat(attendances.findAttendanceTimeAt(attendanceDate))
                .isEqualTo(LocalTime.of(10, 15));
    }
}
