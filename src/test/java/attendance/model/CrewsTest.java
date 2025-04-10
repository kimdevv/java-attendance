package attendance.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.util.List;
import java.util.Map;

import static attendance.model.AttendanceStatusChecker.AttendanceStatus.ABSENT;
import static attendance.model.AttendanceStatusChecker.AttendanceStatus.ATTENDANCE;
import static attendance.model.AttendanceStatusChecker.AttendanceStatus.LATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CrewsTest {

    private Crews crews;

    @BeforeEach
    void initialize() {
        Crew crew1 = Crew.generate("크루1");
        crew1.addAttendance(Year.of(2025).atMonth(4).atDay(1).atTime(10, 0));
        crew1.addAttendance(Year.of(2025).atMonth(4).atDay(2).atTime(10, 0));
        crew1.addAttendance(Year.of(2025).atMonth(4).atDay(3).atTime(10, 5));
        crew1.addAttendance(Year.of(2025).atMonth(4).atDay(4).atTime(10, 30));
        crew1.addAttendance(Year.of(2025).atMonth(4).atDay(7).atTime(13, 31));
        Crew crew2 = Crew.generate("크루2");
        crew2.addAttendance(Year.of(2025).atMonth(4).atDay(1).atTime(10, 0));
        crew2.addAttendance(Year.of(2025).atMonth(4).atDay(2).atTime(10, 0));
        crew2.addAttendance(Year.of(2025).atMonth(4).atDay(3).atTime(10, 5));
        crew2.addAttendance(Year.of(2025).atMonth(4).atDay(4).atTime(10, 31));
        crew2.addAttendance(Year.of(2025).atMonth(4).atDay(7).atTime(13, 31));
        Crew crew3 = Crew.generate("크루3");
        crew3.addAttendance(Year.of(2025).atMonth(4).atDay(1).atTime(10, 0));
        crew3.addAttendance(Year.of(2025).atMonth(4).atDay(2).atTime(10, 0));
        crew3.addAttendance(Year.of(2025).atMonth(4).atDay(3).atTime(10, 31));
        crew3.addAttendance(Year.of(2025).atMonth(4).atDay(4).atTime(10, 31));
        crew3.addAttendance(Year.of(2025).atMonth(4).atDay(7).atTime(13, 31));
        Crew crew4 = Crew.generate("크루4");
        crew4.addAttendance(Year.of(2025).atMonth(4).atDay(1).atTime(10, 31));
        crew4.addAttendance(Year.of(2025).atMonth(4).atDay(2).atTime(10, 31));
        crew4.addAttendance(Year.of(2025).atMonth(4).atDay(3).atTime(10, 31));
        crew4.addAttendance(Year.of(2025).atMonth(4).atDay(4).atTime(10, 31));
        crew4.addAttendance(Year.of(2025).atMonth(4).atDay(7).atTime(13, 31));
        crew4.addAttendance(Year.of(2025).atMonth(4).atDay(8).atTime(10, 31));
        crew4.addAttendance(LocalDate.now(), LocalTime.of(10, 0));
        crews = new Crews(List.of(crew1, crew2, crew3, crew4));
    }

    @Test
    void 오늘_출석_가능한지_테스트한다() {
        // Given
        // When
        // Then
        assertThatCode(() -> crews.validateCanAttendToday("크루1"))
                .doesNotThrowAnyException();
        assertThatThrownBy(() -> crews.validateCanAttendToday("크루4"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("오늘은 이미 출석하셨습니다. 출석 수정 기능을 이용해 주세요.");
    }

    @Test
    void 출석을_등록한다() {
        // Given
        LocalDate attendanceDate = LocalDate.now();
        LocalTime attendanceTime = LocalTime.of(10, 5);

        // When & Then
        assertThatCode(() -> crews.addCrewAttendance("크루1", attendanceDate, attendanceTime))
                .doesNotThrowAnyException();
    }

    @Test
    void 등록되지_않은_크루에게는_출석과_관련된_기능을_제공하지_않는다() {
        // Given
        LocalDate attendanceDate = LocalDate.now();
        LocalTime attendanceTime = LocalTime.of(10, 5);

        // When & Then
        assertThatThrownBy(() -> crews.addCrewAttendance("크루", attendanceDate, attendanceTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 크루의 닉네임입니다.");
    }

    @Test
    void 출석한_날짜의_출석_시간을_확인한다() {
        // Given
        LocalDate attendanceDate = LocalDate.now();
        LocalTime attendanceTime = LocalTime.of(10, 5);
        crews.addCrewAttendance("크루1", attendanceDate, attendanceTime);

        // When & Then
        assertThat(crews.findCrewAttendanceTimeAt("크루1", attendanceDate))
                .isEqualTo(LocalTime.of(10, 5));
    }

    @Test
    void 출석하지_않은_날짜의_시간을_확인하려고_하면_예외가_발생한다() {
        // Given
        LocalDate notAttendanceDate = LocalDate.now();

        // When & Then
        assertThatThrownBy(() -> crews.findCrewAttendanceTimeAt("크루1", notAttendanceDate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 날짜에 출석하지 않았습니다.");
    }

    @Test
    void 출석한_시간을_수정한다() {
        // Given
        LocalDate attendanceDate = LocalDate.now();
        LocalTime originalAttendanceTime = LocalTime.of(10, 5);
        LocalTime newAttendanceTime = LocalTime.of(10, 15);
        crews.addCrewAttendance("크루1", attendanceDate, originalAttendanceTime);

        // When
        crews.modifyCrewAttendanceTime("크루1", attendanceDate, newAttendanceTime);

        // Then
        assertThat(crews.findCrewAttendanceTimeAt("크루1", attendanceDate))
                .isEqualTo(LocalTime.of(10, 15));
    }

    @Test
    void 출석_기록을_가지고_총_출석_상태를_계산한다() {
        // Given
        crews.addCrewAttendance("크루1", Year.of(2025).atMonth(4).atDay(7), LocalTime.of(13, 0)); // 월
        crews.addCrewAttendance("크루1", Year.of(2025).atMonth(4).atDay(8), LocalTime.of(10, 5)); // 화
        crews.addCrewAttendance("크루1", Year.of(2025).atMonth(4).atDay(9), LocalTime.of(10, 6)); // 수
        crews.addCrewAttendance("크루1", Year.of(2025).atMonth(4).atDay(10), LocalTime.of(10, 30)); // 목
        crews.addCrewAttendance("크루1", Year.of(2025).atMonth(4).atDay(11), LocalTime.of(10, 31)); // 금

        // When & Then
        assertThat(crews.calculateCrewAttendanceStatuses("크루1"))
                .isEqualTo(Map.of(
                        ATTENDANCE, 5L,
                        LATE, 3L,
                        ABSENT, 1L
                ));
    }

    @Test
    void 제적_위험자인지_검사한다() {
        // Given
        // When & Then
        assertThat(crews.calculateCrewExpulsionStatus("크루1")).isEqualTo(ExpulsionStatus.NONE);
        assertThat(crews.calculateCrewExpulsionStatus("크루2")).isEqualTo(ExpulsionStatus.WARNING);
        assertThat(crews.calculateCrewExpulsionStatus("크루3")).isEqualTo(ExpulsionStatus.INTERVIEW);
        assertThat(crews.calculateCrewExpulsionStatus("크루4")).isEqualTo(ExpulsionStatus.EXPULSION);
    }

    @Test
    void 제적_위험자들을_가져온다() {
        // Given
        // When & Then
        assertThat(crews.findExpulsionCrews())
                .isEqualTo(List.of(
                        Crew.generate("크루2"),
                        Crew.generate("크루3"),
                        Crew.generate("크루4")
                ));
    }
}
