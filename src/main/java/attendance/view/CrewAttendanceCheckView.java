package attendance.view;

import attendance.model.AttendanceDateTime;
import attendance.model.AttendanceStatusChecker;
import attendance.model.Crew;
import attendance.model.ExpulsionStatus;

import java.time.LocalDateTime;
import java.util.*;

import static attendance.model.AttendanceStatusChecker.*;
import static attendance.view.GeneralView.*;

public class CrewAttendanceCheckView {

    public String readCrewNickname() {
        System.out.println("닉네임을 입력해 주세요.");
        return readOneLine();
    }

    public void printCrewAttendances(final Crew crew, final List<AttendanceDateTime> crewAttendanceDateTimes) {
        System.out.println("이번 달 %s의 출석 기록입니다.\n".formatted(crew.getNickname()));
        for (AttendanceDateTime crewAttendanceDateTime : crewAttendanceDateTimes) {
            AttendanceStatus attendanceStatus = AttendanceStatusChecker.checkStatus(crewAttendanceDateTime);
            printAttendanceDateTimeAndAttendanceStatus(crewAttendanceDateTime, attendanceStatus);
        }
        System.out.println();
    }

    private void printAttendanceDateTimeAndAttendanceStatus(AttendanceDateTime crewAttendanceDateTime, AttendanceStatus attendanceStatus) {
        LocalDateTime dateTime = crewAttendanceDateTime.getLocalDateTime();
        if (dateTime.toLocalTime().equals(AttendanceDateTime.ABSENT_TIME)) {
            System.out.println(DATE_FORMATTER.format(dateTime) + " --:-- (결석)");
            return;
        }
        System.out.println(DATE_TIME_FORMATTER.format(dateTime)
                + " (%s)".formatted(AttendanceStatusTextMaker.make(attendanceStatus)));
    }

    public void printAttendanceStatuses(final Map<AttendanceStatus, Long> attendanceStatuses) {
        for (AttendanceStatus attendanceStatus : AttendanceStatus.values()) {
            String attendanceStatusText = AttendanceStatusTextMaker.make(attendanceStatus);
            Long statusCount = attendanceStatuses.get(attendanceStatus);
            System.out.println("%s: %d회".formatted(attendanceStatusText, statusCount));
        }
        System.out.println();
    }

    public void printExpulsionStatus(final ExpulsionStatus expulsionStatus) {
        if (expulsionStatus.equals(ExpulsionStatus.NONE)) {
            return;
        }
        System.out.println("%s 대상자입니다.".formatted(ExpulsionStatusTextMaker.make(expulsionStatus)));
    }
}
