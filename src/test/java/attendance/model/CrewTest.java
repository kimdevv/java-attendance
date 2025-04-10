package attendance.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.util.Map;

import static attendance.model.AttendanceStatusChecker.AttendanceStatus.ABSENT;
import static attendance.model.AttendanceStatusChecker.AttendanceStatus.ATTENDANCE;
import static attendance.model.AttendanceStatusChecker.AttendanceStatus.LATE;
import static org.assertj.core.api.Assertions.*;

public class CrewTest {

    @ValueSource(strings = {"일이", "일   이", "일이삼사오", "일이삼사    오"})
    @ParameterizedTest
    void _2글자_이상_5글자_이하의_닉네임으로_크루를_생성한다(String nickname) {
        // Given
        // When
        // Then
        assertThat(Crew.generate(nickname))
                .isInstanceOf(Crew.class);
    }

    @ValueSource(strings = {"일", "일이삼사오육"})
    @ParameterizedTest
    void 닉네임은_2글자_이상_5글자_이하가_아니라면_예외가_발생한다(String nickname) {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> Crew.generate(nickname))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("크루의 닉네임은 공백 제외 2글자 이상, 5글자 이하로 입력해 주세요.");
    }

    @Test
    void 출석을_등록한다() {
        // Given
        Crew crew = Crew.generate("크루");
        LocalDate attendanceDate = LocalDate.now();
        LocalTime attendanceTime = LocalTime.of(10, 5);

        // When & Then
        assertThatCode(() -> crew.addAttendance(attendanceDate, attendanceTime))
                .doesNotThrowAnyException();
    }

    @Test
    void 크루가_해당_날짜에_출석했는지_확인한다() {
        // Given
        Crew crew = Crew.generate("크루1");
        Crew crew2 = Crew.generate("크루2");
        LocalDate attendanceDate = LocalDate.now();
        LocalTime attendanceTime = LocalTime.of(10, 5);

        // When
        crew.addAttendance(attendanceDate, attendanceTime);

        // Then
        assertThat(crew.isAttendedAt(attendanceDate)).isTrue();
        assertThat(crew.isAttendedToday()).isTrue();
        assertThat(crew2.isAttendedAt(attendanceDate)).isFalse();
        assertThat(crew2.isAttendedToday()).isFalse();
    }

    @Test
    void 출석한_날짜의_출석_시간을_확인한다() {
        // Given
        Crew crew = Crew.generate("크루");
        LocalDate attendanceDate = LocalDate.now();
        LocalTime attendanceTime = LocalTime.of(10, 5);
        crew.addAttendance(attendanceDate, attendanceTime);

        // When & Then
        assertThat(crew.findAttendanceTimeAt(attendanceDate))
                .isEqualTo(LocalTime.of(10, 5));
    }

    @Test
    void 출석하지_않은_날짜의_시간을_확인하려고_하면_예외가_발생한다() {
        // Given
        Crew crew = Crew.generate("크루");
        LocalDate notAttendanceDate = LocalDate.now();

        // When & Then
        assertThatThrownBy(() -> crew.findAttendanceTimeAt(notAttendanceDate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 날짜에 출석하지 않았습니다.");
    }

    @Test
    void 출석한_시간을_수정한다() {
        // Given
        Crew crew = Crew.generate("크루");
        LocalDate attendanceDate = LocalDate.now();
        LocalTime originalAttendanceTime = LocalTime.of(10, 5);
        LocalTime newAttendanceTime = LocalTime.of(10, 15);
        crew.addAttendance(attendanceDate, originalAttendanceTime);

        // When
        crew.modifyAttendanceTime(attendanceDate, newAttendanceTime);

        // Then
        assertThat(crew.findAttendanceTimeAt(attendanceDate))
                .isEqualTo(LocalTime.of(10, 15));
    }

    @Test
    void 출석_기록을_가지고_총_출석_상태를_계산한다() {
        // Given
        Crew crew = Crew.generate("크루");
        crew.addAttendance(Year.of(2025).atMonth(4).atDay(7).atTime(13, 0)); // 월
        crew.addAttendance(Year.of(2025).atMonth(4).atDay(8).atTime(10, 5)); // 화
        crew.addAttendance(Year.of(2025).atMonth(4).atDay(9).atTime(10, 6)); // 수
        crew.addAttendance(Year.of(2025).atMonth(4).atDay(10).atTime(10, 30)); // 목
        crew.addAttendance(Year.of(2025).atMonth(4).atDay(11).atTime(10, 31)); // 금

        // When & Then
        assertThat(crew.calculateAttendanceStatuses())
                .isEqualTo(Map.of(
                        ATTENDANCE, 2L,
                        LATE, 2L,
                        ABSENT, 1L
                ));
    }

    @Test
    void 제적_위험자인지_검사한다() {
        // Given
        Crew noneCrew = Crew.generate("크루1");
        noneCrew.addAttendance(Year.of(2025).atMonth(4).atDay(7).atTime(13, 0)); // 월
        noneCrew.addAttendance(Year.of(2025).atMonth(4).atDay(8).atTime(10, 0)); // 화
        noneCrew.addAttendance(Year.of(2025).atMonth(4).atDay(9).atTime(10, 5)); // 수
        noneCrew.addAttendance(Year.of(2025).atMonth(4).atDay(10).atTime(10, 30)); // 목
        noneCrew.addAttendance(Year.of(2025).atMonth(4).atDay(11).atTime(10, 31)); // 금
        Crew warningCrew = Crew.generate("크루2");
        warningCrew.addAttendance(Year.of(2025).atMonth(4).atDay(7).atTime(13, 0)); // 월
        warningCrew.addAttendance(Year.of(2025).atMonth(4).atDay(8).atTime(10, 0)); // 화
        warningCrew.addAttendance(Year.of(2025).atMonth(4).atDay(9).atTime(10, 5)); // 수
        warningCrew.addAttendance(Year.of(2025).atMonth(4).atDay(10).atTime(10, 31)); // 목
        warningCrew.addAttendance(Year.of(2025).atMonth(4).atDay(11).atTime(10, 31)); // 금
        Crew interviewCrew = Crew.generate("크루3");
        interviewCrew.addAttendance(Year.of(2025).atMonth(4).atDay(7).atTime(13, 0)); // 월
        interviewCrew.addAttendance(Year.of(2025).atMonth(4).atDay(8).atTime(10, 0)); // 화
        interviewCrew.addAttendance(Year.of(2025).atMonth(4).atDay(9).atTime(10, 31)); // 수
        interviewCrew.addAttendance(Year.of(2025).atMonth(4).atDay(10).atTime(10, 31)); // 목
        interviewCrew.addAttendance(Year.of(2025).atMonth(4).atDay(11).atTime(10, 31)); // 금
        Crew expulsionCrew = Crew.generate("크루4");
        expulsionCrew.addAttendance(Year.of(2025).atMonth(4).atDay(7).atTime(13, 31)); // 월
        expulsionCrew.addAttendance(Year.of(2025).atMonth(4).atDay(8).atTime(10, 31)); // 화
        expulsionCrew.addAttendance(Year.of(2025).atMonth(4).atDay(9).atTime(10, 31)); // 수
        expulsionCrew.addAttendance(Year.of(2025).atMonth(4).atDay(10).atTime(10, 31)); // 목
        expulsionCrew.addAttendance(Year.of(2025).atMonth(4).atDay(11).atTime(10, 31)); // 금
        expulsionCrew.addAttendance(Year.of(2025).atMonth(4).atDay(15).atTime(10, 31)); // 화

        // When & Then
        assertThat(noneCrew.calculateExpulsionStatus()).isEqualTo(ExpulsionStatus.NONE);
        assertThat(warningCrew.calculateExpulsionStatus()).isEqualTo(ExpulsionStatus.WARNING);
        assertThat(interviewCrew.calculateExpulsionStatus()).isEqualTo(ExpulsionStatus.INTERVIEW);
        assertThat(expulsionCrew.calculateExpulsionStatus()).isEqualTo(ExpulsionStatus.EXPULSION);
    }
    
    @Test
    void 지각_3회를_결석_1회로_간주한_총_결석_횟수를_계산한다() {
        // Given
        Crew crew = Crew.generate("크루");
        crew.addAttendance(Year.of(2025).atMonth(4).atDay(7).atTime(13, 6)); // 월
        crew.addAttendance(Year.of(2025).atMonth(4).atDay(8).atTime(10, 6)); // 화
        crew.addAttendance(Year.of(2025).atMonth(4).atDay(9).atTime(10, 6)); // 수
        crew.addAttendance(Year.of(2025).atMonth(4).atDay(10).atTime(10, 6)); // 목
        crew.addAttendance(Year.of(2025).atMonth(4).atDay(11).atTime(10, 31)); // 금
        
        // When & Then
        assertThat(crew.calculateTotalAbsentCounts()).isEqualTo(2);
    }
}
