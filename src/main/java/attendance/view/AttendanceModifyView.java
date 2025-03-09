package attendance.view;

import attendance.domain.AttendanceDateTime;
import attendance.domain.AttendanceStatusChecker;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import static attendance.view.GeneralView.*;

public class AttendanceModifyView {

    public String readCrewNickname() {
        System.out.println("출석을 수정하려는 크루의 닉네임을 입력해 주세요.");
        return readOneLine();
    }

    public int readDayToModify() {
        System.out.println("수정하려는 날짜(일)를 입력해 주세요.");
        String numberText = readOneLine();
        return NumberParser.parse(numberText);
    }

    public LocalTime readTimeToModify() {
        System.out.println("언제로 변경하겠습니까?");
        String timeInput = readOneLine();
        try {
            return LocalTime.parse(timeInput, TIME_FORMATTER);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("시간을 올바르게 입력해 주세요.");
        }
    }

    public void printAttendanceModifyResult(final AttendanceDateTime originalDateTime, final AttendanceDateTime newDateTime) {
        LocalDateTime originalLocalDateTime = originalDateTime.getLocalDateTime();
        String originalAttendanceStatusText = AttendanceStatusTextMaker.make(AttendanceStatusChecker.checkStatus(originalDateTime));
        LocalDateTime newLocalDateTIme = newDateTime.getLocalDateTime();
        String newAttendanceStatusText = AttendanceStatusTextMaker.make(AttendanceStatusChecker.checkStatus(newDateTime));
        System.out.printf(DATE_TIME_FORMATTER.format(originalLocalDateTime) + " (%s)".formatted(originalAttendanceStatusText)
                + " -> " + TIME_FORMATTER.format(newLocalDateTIme) + " (%s)".formatted(newAttendanceStatusText)
                + " 수정 완료!");
    }
}
