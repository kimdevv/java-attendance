package attendance.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.util.Map;

import static attendance.model.AttendanceStatusChecker.*;
import static org.assertj.core.api.Assertions.*;

public class AttendanceStatusCheckerTest {

    @CsvSource({
            "24, 13, 0, ATTENDANCE",
            "24, 13, 5, ATTENDANCE",
            "24, 13, 6, LATE",
            "24, 13, 30, LATE",
            "24, 13, 31, ABSENT",
            "25, 10, 0, ATTENDANCE",
            "25, 10, 5, ATTENDANCE",
            "25, 10, 6, LATE",
            "25, 10, 30, LATE",
            "25, 10, 31, ABSENT",
    })
    @ParameterizedTest
    void 출석_시간을_주면_출석_상태를_알려준다(int day, int hour, int minute, AttendanceStatus expected) {
        // Given
        LocalDate attendanceDate = Year.of(2025).atMonth(2).atDay(day);
        LocalTime attendanceTime = LocalTime.of(hour, minute);

        // When
        AttendanceStatus attendanceStatus = checkStatus(attendanceDate, attendanceTime);

        // Then
        assertThat(attendanceStatus).isEqualTo(expected);
    }

    @Test
    void 출석_시간들을_주면_출석_상태들을_계산해서_알려준다() {
        // Given
        Map<LocalDate, LocalTime> attendances = Map.of(
                Year.of(2025).atMonth(4).atDay(1), LocalTime.of(10, 0), // 출석
                Year.of(2025).atMonth(4).atDay(2), LocalTime.of(10, 0), // 출석
                Year.of(2025).atMonth(4).atDay(3), LocalTime.of(10, 6), // 지각
                Year.of(2025).atMonth(4).atDay(4), LocalTime.of(10, 6), // 지각
                Year.of(2025).atMonth(4).atDay(7), LocalTime.of(13, 6), // 지각
                Year.of(2025).atMonth(4).atDay(8), LocalTime.of(10, 35), // 결석
                Year.of(2025).atMonth(4).atDay(9), LocalTime.of(19, 35) // 결석
        );
        AttendanceStatusChecker attendanceStatusChecker = new AttendanceStatusChecker();

        // When
        Map<AttendanceStatus, Long> attendanceStatuses = attendanceStatusChecker.checkStatuses(attendances);

        // Then
        assertThat(attendanceStatuses)
                .isEqualTo(Map.of(AttendanceStatus.ATTENDANCE, 2L, AttendanceStatus.LATE, 3L, AttendanceStatus.ABSENT, 2L));
    }
}
