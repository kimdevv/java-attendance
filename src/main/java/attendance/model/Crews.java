package attendance.model;

import java.util.List;

public class Crews {

    private final List<Crew> crews;

    public Crews(final List<Crew> crews) {
        this.crews = crews;
    }

    public Crew findByNickname(final String nickname) {
        return crews.stream()
                .filter(crew -> crew.getNickname().equalsIgnoreCase(nickname))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 크루의 닉네임입니다."));
    }

    public List<Crew> getCrews() {
        return crews;
    }
}
