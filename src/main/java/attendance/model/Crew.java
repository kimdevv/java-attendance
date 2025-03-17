package attendance.model;

import java.util.Objects;

public class Crew {

    public static final int MINIMUM_NICKNAME_LENGTH = 2;
    public static final int MAXIMUM_NICKNAME_LENGTH = 5;

    private final String nickname;

    public Crew(final String nickname) {
        validateLength(nickname);
        this.nickname = nickname;
    }

    private void validateLength(final String nickname) {
        final int nicknameLength = nickname.replace(" ", "").length();
        if (!(MINIMUM_NICKNAME_LENGTH <= nicknameLength && nicknameLength <= MAXIMUM_NICKNAME_LENGTH)) {
            throw new IllegalArgumentException("크루의 닉네임은 공백 제외 2글자 이상, 5글자 이하로 입력해 주세요.");
        }
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Crew crew = (Crew) o;
        return Objects.equals(nickname, crew.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickname);
    }
}
