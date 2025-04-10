package attendance.model;

import java.util.Arrays;

public enum ExpulsionStatus {
    EXPULSION(6),
    INTERVIEW(3),
    WARNING(2),
    NONE(0);

    private final int absentStandard;

    ExpulsionStatus(int absentStandard) {
        this.absentStandard = absentStandard;
    }

    public static ExpulsionStatus from(final int absentCount) {
        return Arrays.stream(values())
                .filter(expulsionStatus -> expulsionStatus.absentStandard <= absentCount)
                .findAny()
                .orElse(NONE);
    }
}
