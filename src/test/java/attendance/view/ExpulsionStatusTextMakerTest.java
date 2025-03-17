package attendance.view;

import attendance.model.ExpulsionStatus;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class ExpulsionStatusTextMakerTest {

    @CsvSource({
            "WARNING, 경고",
            "INTERVIEW, 면담",
            "EXPULSION, 제적",
    })
    @ParameterizedTest
    void 제적상태를_알려주면_그에_해당하는_문자열을_알려준다(ExpulsionStatus expulsionStatus, String expected) {
        // When
        String actual = ExpulsionStatusTextMaker.make(expulsionStatus);

        // Then
        assertThat(actual).isEqualTo(expected);
    }
}
