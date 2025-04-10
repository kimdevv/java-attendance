//package attendance.model;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.Year;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import static org.assertj.core.api.Assertions.*;
//
//public class AttendanceBookTest {
//
//    private AttendanceBook attendanceBook;
//
//    @BeforeEach
//    void 출석부_초기화() {
//        Crew cookie = new Crew("쿠키");
//        List<AttendanceDateTime> cookieAttendanceDateTimes = new ArrayList<>(List.of(
//                new AttendanceDateTime(Year.of(2025).atMonth(2).atDay(25).atTime(10, 4))
//        ));
//        Attendances cookieAttendances = new Attendances(cookieAttendanceDateTimes);
//        Map<Crew, Attendances> crewAttendances = Map.of(cookie, cookieAttendances);
//        this.attendanceBook = new AttendanceBook(crewAttendances);
//    }
//
//    @Test
//    void 크루들의_이전_출석_기록들을_바탕으로_출석부를_생성한다() {
//        // Given
//        Crew cookie = new Crew("쿠키");
//        List<AttendanceDateTime> cookieAttendanceDateTimes = List.of(
//                new AttendanceDateTime(Year.of(2025).atMonth(2).atDay(25).atTime(10, 4)));
//        Attendances cookieAttendances = new Attendances(cookieAttendanceDateTimes);
//        Crew bingbong = new Crew("빙봉");
//        List<AttendanceDateTime> bingbongAttendanceDateTimes = List.of(
//                new AttendanceDateTime(Year.of(2025).atMonth(2).atDay(26).atTime(10, 7)),
//                new AttendanceDateTime(Year.of(2025).atMonth(2).atDay(27).atTime(10, 35)));
//        Attendances bingbongAttendances = new Attendances(bingbongAttendanceDateTimes);
//        Map<Crew, Attendances> crewAttendances = Map.of(cookie, cookieAttendances, bingbong, bingbongAttendances);
//
//        // When & Then
//        assertThatCode(() -> new AttendanceBook(crewAttendances))
//                .doesNotThrowAnyException();
//    }
//
//    @Test
//    void 등록된_크루인지_검증한다() {
//        // Given
//        Crew cookie = new Crew("쿠키");
//
//        // When & Then
//        assertThatCode(() -> attendanceBook.validateRegisteredCrew(cookie))
//                .doesNotThrowAnyException();
//    }
//
//    @Test
//    void 등록된_크루가_아닌_경우를_찾아낸다() {
//        // Given
//        Crew unregisteredCrew = new Crew("쿠키2");
//
//        // When & Then
//        assertThatThrownBy(() -> attendanceBook.validateRegisteredCrew(unregisteredCrew))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("등록되지 않은 닉네임입니다.");
//    }
//
//    @Test
//    void 이미_출석된_날짜인지_검사한다() {
//        // Given
//        Crew cookie = new Crew("쿠키");
//        LocalDateTime localDateTime = Year.of(2025).atMonth(2).atDay(25).atTime(10, 0);
//        AttendanceDateTime attendanceDateTime = new AttendanceDateTime(localDateTime);
//
//        // When & Then
//        assertThatThrownBy(() -> attendanceBook.validateDuplicateAttendanceDate(cookie, attendanceDateTime))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("오늘은 이미 출석하셨습니다. 출석 수정 기능을 이용해 주세요.");
//    }
//
//    @Test
//    void 크루의_출석을_등록한다() {
//        // Given
//        Crew crew = new Crew("쿠키");
//        LocalDateTime localDateTime = Year.of(2025).atMonth(2).atDay(26).atTime(10, 0);
//        AttendanceDateTime attendanceDateTime = new AttendanceDateTime(localDateTime);
//
//        // When
//        attendanceBook.saveAttendanceDateTime(crew, attendanceDateTime);
//
//        // Then
//        assertThatThrownBy(() -> attendanceBook.validateDuplicateAttendanceDate(crew, attendanceDateTime))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("오늘은 이미 출석하셨습니다. 출석 수정 기능을 이용해 주세요.");
//    }
//
//    @Test
//    void 크루와_date를_알려주면_해당_날짜의_출석시간_객체를_알려준다() {
//        // Given
//        Crew crew = new Crew("쿠키");
//        LocalDate findDate = Year.of(2025).atMonth(2).atDay(25);
//        AttendanceDateTime expected = new AttendanceDateTime(Year.of(2025).atMonth(2).atDay(25).atTime(10, 4));
//
//        // When
//        AttendanceDateTime actual = attendanceBook.findAttendanceDateTimeByCrewAndDate(crew, findDate);
//
//        // Then
//        assertThat(actual).isEqualTo(expected);
//    }
//
//    @Test
//    void 크루와_date를_알려주지만_해당_date에_출석하지_않았을_경우() {
//        // Given
//        Crew crew = new Crew("쿠키");
//        LocalDate findDate = Year.of(2025).atMonth(2).atDay(28);
//
//        // Then
//        assertThatThrownBy(() -> attendanceBook.findAttendanceDateTimeByCrewAndDate(crew, findDate))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("해당 일자에 출석하지 않았습니다.");
//    }
//}
