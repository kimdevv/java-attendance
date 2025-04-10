package attendance.model;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.time.Year;

import static org.assertj.core.api.Assertions.assertThat;

public class CrewInitializerTest {

    @Test
    void 파일로부터_크루_정보를_생성한다() {
        // Given
        // When
        Crews crews = CrewsInitializer.initializeFromAttendanceFile();

        // Then
        // 2025-04-02 10:07
        assertThat(crews.findCrewAttendanceTimeAt("쿠키", Year.of(2025).atMonth(4).atDay(2)))
                .isEqualTo(LocalTime.of(10, 8));
        assertThat(crews.findCrewAttendanceTimeAt("빙봉", Year.of(2025).atMonth(4).atDay(2)))
                .isEqualTo(LocalTime.of(10, 7));
        assertThat(crews.findCrewAttendanceTimeAt("빙티", Year.of(2025).atMonth(4).atDay(2)))
                .isEqualTo(LocalTime.of(10, 7));
        assertThat(crews.findCrewAttendanceTimeAt("이든", Year.of(2025).atMonth(4).atDay(2)))
                .isEqualTo(LocalTime.of(10, 7));
    }
}
