//package attendance.model;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.CsvSource;
//
//import java.time.LocalDate;
//import java.time.Year;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.*;
//
//public class AttendancesTest {
//
//    private Attendances attendances;
//
//    @BeforeEach
//    void 초기화() {
//        List<AttendanceDateTime> attendanceDateTimes = new ArrayList<>(List.of(
//                new AttendanceDateTime(Year.of(2025).atMonth(2).atDay(25).atTime(10, 4)),
//                new AttendanceDateTime(Year.of(2025).atMonth(2).atDay(26).atTime(10, 4)),
//                new AttendanceDateTime(Year.of(2025).atMonth(2).atDay(27).atTime(10, 4))
//        ));
//        this.attendances = new Attendances(attendanceDateTimes);
//    }
//
//    @CsvSource({
//            "27, true",
//            "28, false"
//    })
//    @ParameterizedTest
//    void 주어진_date에_저장된_출석날짜_객체가_있는지_확인한다(int day, boolean expected) {
//        // Given
//        AttendanceDateTime findDateTime = new AttendanceDateTime(Year.of(2025).atMonth(2).atDay(day).atTime(10, 0));
//
//        // When & Then
//        assertThat(attendances.isSameDateExists(findDateTime)).isEqualTo(expected);
//    }
//
//    @Test
//    void 날짜가_주어지면_해당_날짜의_출석날짜_객체를_반환한다() {
//        // Given
//        LocalDate findDate = Year.of(2025).atMonth(2).atDay(27);
//
//        // When & Then
//        assertThat(attendances.findByLocalDate(findDate))
//                .isEqualTo(new AttendanceDateTime(Year.of(2025).atMonth(2).atDay(27).atTime(10, 4)));
//    }
//
//    @Test
//    void 출석하지_않은_날짜로_찾으면_출석날짜_객체를_반환하지_않는다() {
//        // Given
//        LocalDate absentDate = Year.of(2025).atMonth(2).atDay(28);
//
//        // When & Then
//        assertThatThrownBy(() -> attendances.findByLocalDate(absentDate))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("해당 일자에 출석하지 않았습니다.");
//    }
//}
