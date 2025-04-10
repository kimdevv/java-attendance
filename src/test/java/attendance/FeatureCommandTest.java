package attendance;

import attendance.FeatureCommand;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;

public class FeatureCommandTest {

    @CsvSource({
            "1, ATTENDANCE_CONFIRMATION",
            "2, ATTENDANCE_MODIFICATION",
            "3, CREW_ATTENDANCE_CHECK",
            "4, EXPULSION_CREW_CHECK",
            "Q, QUIT",
            "q, QUIT",
    })
    @ParameterizedTest
    void 명령어_문자열에_해당하는_FeatureCommand_enum을_반환한다(String commandText, FeatureCommand expected) {
        // When & Then
        assertThat(FeatureCommand.from(commandText)).isEqualTo(expected);
    }

    @Test
    void 잘못된_명령어를_입력하면_반환하지_않는다() {
        // Given
        String invalidCommandText = "a";

        // When
        assertThatThrownBy(() -> FeatureCommand.from(invalidCommandText))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 기능입니다.");
    }
}
