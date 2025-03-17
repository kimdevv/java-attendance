package attendance.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;

public class ExpulsionStatusTest {

    @CsvSource({
            "1, NONE",
            "2, WARNING",
            "3, INTERVIEW",
            "6, EXPULSION"
    })
    @ParameterizedTest
    void 제적_대상자에_해당하는지_검사한다(long absentCount, ExpulsionStatus expected) {
        // When
        assertThat(ExpulsionStatus.from(absentCount))
                .isEqualTo(expected);
    }
}
