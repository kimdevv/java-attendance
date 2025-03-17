package attendance.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

public class CrewTest {
    @Test
    void 크루의_닉네임을_통해_크루를_생성한다() {
        // Given
        String nickname = "프리";

        // When & Then
        assertThatCode(() -> new Crew(nickname))
                .doesNotThrowAnyException();
    }

    @ValueSource(strings = {"   ", "일", "일이삼사오육"})
    @ParameterizedTest
    void 닉네임이_2글자_이상_5글자_이하가_아니라면_크루를_생성하지_못한다(String nickname) {
        // Given

        // When & Then
        assertThatThrownBy(() -> new Crew(nickname))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("크루의 닉네임은 공백 제외 2글자 이상, 5글자 이하로 입력해 주세요.");
    }
}
