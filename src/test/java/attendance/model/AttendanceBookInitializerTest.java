//package attendance.model;
//
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//
//import java.time.Year;
//import java.util.List;
//import java.util.Map;
//
//public class AttendanceBookInitializerTest {
//
//    @Test
//    void 읽어온_출석_기록들을_가지고_출석부_객체를_생성한다() {
//        // Given
//        AttendanceBookInitializer attendanceBookInitializer = new AttendanceBookInitializer();
//        List<String> crewAttendanceTexts = List.of("쿠키,2025-02-25 10:04", "빙봉,2025-02-26 10:07", "빙봉,2025-02-27 10:35");
//
//        Crew cookie = new Crew("쿠키");
//        List<AttendanceDateTime> cookieAttendanceDateTimes = List.of(
//                new AttendanceDateTime(Year.of(2025).atMonth(2).atDay(25).atTime(10, 4)));
//        Attendances cookieAttendances = new Attendances(cookieAttendanceDateTimes);
//        Crew bingbong = new Crew("빙봉");
//        List<AttendanceDateTime> bingbongAttendanceDateTimes = List.of(
//                new AttendanceDateTime(Year.of(2025).atMonth(2).atDay(26).atTime(10, 7)),
//                new AttendanceDateTime(Year.of(2025).atMonth(2).atDay(27).atTime(10, 35)));
//        Attendances bingbongAttendances = new Attendances(bingbongAttendanceDateTimes);
//        AttendanceBook expected = new AttendanceBook(Map.of(cookie, cookieAttendances, bingbong, bingbongAttendances));
//
//        // When
//        AttendanceBook crewAttendances = attendanceBookInitializer.initialize(crewAttendanceTexts);
//
//        // Then
//        Assertions.assertThat(crewAttendances)
//                .isEqualTo(expected);
//    }
//}
