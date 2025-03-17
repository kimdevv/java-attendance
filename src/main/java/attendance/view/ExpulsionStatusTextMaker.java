package attendance.view;

import attendance.model.ExpulsionStatus;

public class ExpulsionStatusTextMaker {

    public static final String WARNING_TEXT = "경고";
    public static final String INTERVIEW_TEXT = "면담";
    public static final String EXPULSION_TEXT = "제적";
    public static final String NONE_TEXT = "없음";

    public static String make(final ExpulsionStatus expulsionStatus) {
        if (expulsionStatus.equals(ExpulsionStatus.WARNING)) {
            return WARNING_TEXT;
        }
        if (expulsionStatus.equals(ExpulsionStatus.INTERVIEW)) {
            return INTERVIEW_TEXT;
        }
        if (expulsionStatus.equals(ExpulsionStatus.EXPULSION)) {
            return EXPULSION_TEXT;
        }
        return NONE_TEXT;
    }
}
