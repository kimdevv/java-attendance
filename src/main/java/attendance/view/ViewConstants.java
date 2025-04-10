package attendance.view;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ViewConstants {

    static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM월 dd일 E요일", Locale.KOREA);
    static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
}
