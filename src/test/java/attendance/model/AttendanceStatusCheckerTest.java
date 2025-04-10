//package attendance.model;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.CsvSource;
//
//import java.time.Year;
//import java.util.List;
//import java.util.Map;
//
//import static attendance.model.AttendanceStatusChecker.*;
//import static org.assertj.core.api.Assertions.*;
//
//public class AttendanceStatusCheckerTest {
//
//    @CsvSource({
//            "24, 13, 0, ATTENDANCE",
//            "24, 13, 5, ATTENDANCE",
//            "24, 13, 6, LATE",
//            "24, 13, 30, LATE",
//            "24, 13, 31, ABSENT",
//            "25, 10, 0, ATTENDANCE",
//            "25, 10, 5, ATTENDANCE",
//            "25, 10, 6, LATE",
//            "25, 10, 30, LATE",
//            "25, 10, 31, ABSENT",
//    })
//    @ParameterizedTest
//    void 출석_시간을_주면_출석_상태를_알려준다(int day, int hour, int minute, AttendanceStatus expected) {
//        // Given
//        AttendanceDateTime attendanceDateTime = new AttendanceDateTime(
//                Year.of(2025).atMonth(2).atDay(day).atTime(hour, minute));
//
//        // When
//        AttendanceStatus attendanceStatus = checkStatus(attendanceDateTime);
//
//        // Then
//        assertThat(attendanceStatus).isEqualTo(expected);
//    }
//
//    @Test
//    void 출석_시간들을_주면_출석_상태들을_계산해서_알려준다() {
//        // Given
//        List<AttendanceDateTime> attendanceDateTimes = List.of(
//                new AttendanceDateTime(Year.of(2025).atMonth(2).atDay(18).atTime(10, 0)), // 출석
//                new AttendanceDateTime(Year.of(2025).atMonth(2).atDay(19).atTime(10, 0)), // 출석
//                new AttendanceDateTime(Year.of(2025).atMonth(2).atDay(20).atTime(10, 6)), // 지각
//                new AttendanceDateTime(Year.of(2025).atMonth(2).atDay(21).atTime(10, 6)), // 지각
//                new AttendanceDateTime(Year.of(2025).atMonth(2).atDay(24).atTime(13, 6)), // 지각
//                new AttendanceDateTime(Year.of(2025).atMonth(2).atDay(25).atTime(10, 35)), // 결석
//                new AttendanceDateTime(Year.of(2025).atMonth(2).atDay(26).atTime(19, 35)) // 결석
//        );
//        AttendanceStatusChecker attendanceStatusChecker = new AttendanceStatusChecker();
//
//        // When
//        Map<AttendanceStatus, Long> attendanceStatuses = attendanceStatusChecker.checkStatuses(attendanceDateTimes);
//
//        // Then
//        assertThat(attendanceStatuses)
//                .isEqualTo(Map.of(AttendanceStatus.ATTENDANCE, 2L, AttendanceStatus.LATE, 3L, AttendanceStatus.ABSENT, 2L));
//    }
//
//    @Test
//    void 지각_3회를_결석_1회로_치환하여_총_결석_횟수를_알려준다() {
//        // Given
//        List<AttendanceDateTime> attendanceDateTimes = List.of(
//                new AttendanceDateTime(Year.of(2025).atMonth(2).atDay(18).atTime(10, 0)), // 출석
//                new AttendanceDateTime(Year.of(2025).atMonth(2).atDay(19).atTime(10, 0)), // 출석
//                new AttendanceDateTime(Year.of(2025).atMonth(2).atDay(20).atTime(10, 6)), // 지각
//                new AttendanceDateTime(Year.of(2025).atMonth(2).atDay(21).atTime(10, 6)), // 지각
//                new AttendanceDateTime(Year.of(2025).atMonth(2).atDay(24).atTime(13, 6)), // 지각
//                new AttendanceDateTime(Year.of(2025).atMonth(2).atDay(25).atTime(10, 35)), // 결석
//                new AttendanceDateTime(Year.of(2025).atMonth(2).atDay(26).atTime(19, 35)) // 결석
//        );
//        AttendanceStatusChecker attendanceStatusChecker = new AttendanceStatusChecker();
//
//        // When
//        long absentCount = AttendanceStatusChecker.calculateAllAbsent(attendanceDateTimes);
//
//        // Then
//        assertThat(absentCount).isEqualTo(3);
//    }
//}
