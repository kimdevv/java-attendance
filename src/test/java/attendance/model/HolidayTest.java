package attendance.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;
import java.time.Year;

import static org.assertj.core.api.Assertions.*;

public class HolidayTest {

    @CsvSource({
            "1,1,true",
            "1,2,false"
    })
    @ParameterizedTest
    void 주어진_날짜가_공휴일인지_확인한다(int month, int day, boolean expected) {
        // Given
        LocalDateTime dateTime = Year.of(2025).atMonth(month).atDay(day).atTime(10, 00);

        // When & Then
        assertThat(Holiday.isHoliday(dateTime))
                .isEqualTo(expected);
    }
}
