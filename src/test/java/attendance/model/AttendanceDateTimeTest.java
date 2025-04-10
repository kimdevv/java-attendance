//package attendance.model;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.CsvSource;
//
//import java.time.*;
//
//import static org.assertj.core.api.Assertions.*;
//
//public class AttendanceDateTimeTest {
//
//    @Test
//    void 출석_일자와_시간을_알려주면_해당_시간대의_출석시간_객체를_생성한다() {
//        // Given
//        LocalDateTime attendanceDateTime = Year.of(2025).atMonth(2).atDay(26).atTime(10, 0);
//
//        // When & Then
//        assertThatCode(() -> new AttendanceDateTime(attendanceDateTime))
//                .doesNotThrowAnyException();
//    }
//
//    @Test
//    void 주말에는_출석할_수_없다() {
//        // Given
//        LocalDateTime attendanceDateTime = Year.of(2025).atMonth(2).atDay(23).atTime(10, 0);
//
//        // When & Then
//        assertThatThrownBy(() -> new AttendanceDateTime(attendanceDateTime))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("2월 23일 일요일은 등교일이 아닙니다.");
//    }
//
//    @Test
//    void 공휴일에는_출석할_수_없다() {
//        // Given
//        LocalDateTime attendanceDateTime = Year.of(2025).atMonth(1).atDay(1).atTime(10, 0);
//
//        // When & Then
//        assertThatThrownBy(() -> new AttendanceDateTime(attendanceDateTime))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("1월 1일 수요일은 등교일이 아닙니다.");
//    }
//
//    @Test
//    void 캠퍼스_운영시간_외에는_출석할_수_없다() {
//        // Given
//        LocalDateTime attendanceDateTime = Year.of(2025).atMonth(2).atDay(26).atTime(7, 0);
//
//        // When & Then
//        assertThatThrownBy(() -> new AttendanceDateTime(attendanceDateTime))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("7시 0분은 캠퍼스 운영 시간이 아닙니다.");
//    }
//
//    @Test
//    void 미래의_시간에는_출석할_수_없다() {
//        // Given
//        LocalDateTime attendanceDateTime = Year.of(2025).atMonth(12).atDay(26).atTime(10, 0);
//
//        // When & Then
//        assertThatThrownBy(() -> new AttendanceDateTime(attendanceDateTime))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("미래의 시간으로는 출석할 수 없습니다.");
//    }
//
//    @Test
//    void 두_날짜_사이의_차이를_분으로_환산하여_반환한다() {
//        // Given
//        LocalDateTime start = Year.of(2025).atMonth(2).atDay(27).atTime(13, 30);
//        AttendanceDateTime startDateTime = new AttendanceDateTime(start);
//        LocalTime end = LocalTime.of(10, 00);
//
//        // When & Then
//        assertThat(startDateTime.calculateMinuteDifference(end))
//                .isEqualTo(210);
//    }
//
//    @CsvSource({
//            "17, MONDAY",
//            "18, TUESDAY",
//            "19, WEDNESDAY",
//            "20, THURSDAY",
//            "21, FRIDAY"
//    })
//    @ParameterizedTest
//    void 해당_출석날짜의_요일을_알려준다(int day, DayOfWeek expected) {
//        // Given
//        LocalDateTime localDateTime = Year.of(2025).atMonth(2).atDay(day).atTime(13, 30);
//        AttendanceDateTime attendanceDateTime = new AttendanceDateTime(localDateTime);
//
//        // When & Then
//        assertThat(attendanceDateTime.calculateDayOfWeek()).isEqualTo(expected);
//    }
//
//    @CsvSource({
//            "1, false",
//            "2, true"
//    })
//    @ParameterizedTest
//    void 출석한_day가_주어진_day인지_확인한다(int day, boolean expected) {
//        // Given
//        AttendanceDateTime attendanceDateTime = AttendanceDateTime.createAbsentDateTime(Year.of(2025).atMonth(3).atDay(2));
//
//        // When & Then
//        assertThat(attendanceDateTime.isSameDate(LocalDate.now().withDayOfMonth(day)))
//                .isEqualTo(expected);
//    }
//
//    @Test
//    void 변경할_시간을_주면_해당_시간으로_출석시간을_바꿔준다() {
//        // Given
//        LocalDateTime originalDateTime = Year.of(2025).atMonth(2).atDay(27).atTime(13, 30);
//        AttendanceDateTime originalAttendanceDateTime = new AttendanceDateTime(originalDateTime);
//        LocalTime newTime = LocalTime.of(10, 30);
//        AttendanceDateTime expected = new AttendanceDateTime(Year.of(2025).atMonth(2).atDay(27).atTime(10, 30));
//
//        // When
//        AttendanceDateTime newAttendanceDateTime = originalAttendanceDateTime.changeTime(newTime);
//
//        // Then
//        assertThat(newAttendanceDateTime).isEqualTo(expected);
//    }
//}
