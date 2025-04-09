package attendance;

import java.util.Arrays;

public enum FeatureCommand {
    ATTENDANCE_CONFIRMATION("1"),
    ATTENDANCE_MODIFICATION("2"),
    CREW_ATTENDANCE_CHECK("3"),
    EXPULSION_CREW_CHECK("4"),
    QUIT("Q");

    private final String commandText;

    FeatureCommand(final String commandText) {
        this.commandText = commandText;
    }

    public static FeatureCommand from(final String commandText) {
        return Arrays.stream(values())
                .filter(value -> value.commandText.equalsIgnoreCase(commandText))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 기능입니다."));
    }
}
