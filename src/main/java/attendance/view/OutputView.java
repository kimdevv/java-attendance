package attendance.view;

import attendance.dto.CheckExpulsionResultDto;
import attendance.model.AttendanceStatusChecker;
import attendance.model.AttendanceStatusChecker.AttendanceStatus;
import attendance.model.Crew;
import attendance.model.ExpulsionStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OutputView {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM월 dd일 E요일", Locale.KOREA);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MM월 dd일 E요일 HH:mm", Locale.KOREA);

    public void outputExceptionMessage(final String exceptionMessage) {
        System.out.println("[ERROR] " + exceptionMessage);
        System.out.println();
    }

    public void outputAttendanceInformation(final LocalDate attendanceDate, final LocalTime attendanceTime, final AttendanceStatus attendanceStatus) {
        outputAttendanceInformation(LocalDateTime.of(attendanceDate, attendanceTime), attendanceStatus);
    }

    public void outputAttendanceInformation(final LocalDateTime attendanceDateTime, final AttendanceStatus attendanceStatus) {
        System.out.printf("%s", DATE_FORMATTER.format(attendanceDateTime));
        if (attendanceDateTime.toLocalTime() == Crew.ABSENT_TIME) {
            System.out.printf(" --:-- (결석)%n");
            return;
        }
        String attendanceStatusText = AttendanceStatusTextMaker.make(attendanceStatus);
        System.out.printf(" %s (%s)%n", TIME_FORMATTER.format(attendanceDateTime), attendanceStatusText);
    }

    public void outputModifyAttendanceResult(final LocalDate attendanceDate, final LocalTime originalAttendanceTime, final LocalTime newAttendanceTime) {
        String originalAttendanceStatusText = AttendanceStatusTextMaker.make(AttendanceStatusChecker.checkStatus(attendanceDate, originalAttendanceTime));
        String newAttendanceStatusText = AttendanceStatusTextMaker.make(AttendanceStatusChecker.checkStatus(attendanceDate, newAttendanceTime));
        System.out.printf("%s %s (%s) -> %s (%s) 수정 완료!%n%n",
                DATE_FORMATTER.format(attendanceDate),
                TIME_FORMATTER.format(originalAttendanceTime), originalAttendanceStatusText,
                TIME_FORMATTER.format(newAttendanceTime), newAttendanceStatusText);
    }

    public void outputCrewAttendances(final Crew crew, final Map<LocalDate, LocalTime> crewAttendances) {
        System.out.println("이번 달 %s의 출석 기록입니다.\n".formatted(crew.getNickname()));
        for (LocalDate attendanceDate : crewAttendances.keySet()) {
            AttendanceStatus attendanceStatus = AttendanceStatusChecker.checkStatus(attendanceDate, crewAttendances.get(attendanceDate));
            outputAttendanceInformation(attendanceDate, crewAttendances.get(attendanceDate), attendanceStatus);
        }
        System.out.println();
    }

    public void outputAttendanceStatuses(final Map<AttendanceStatus, Long> attendanceStatuses) {
        for (AttendanceStatus attendanceStatus : AttendanceStatus.values()) {
            String attendanceStatusText = AttendanceStatusTextMaker.make(attendanceStatus);
            Long statusCount = attendanceStatuses.get(attendanceStatus);
            System.out.println("%s: %d회".formatted(attendanceStatusText, statusCount));
        }
        System.out.println();
    }

    public void outputExpulsionStatus(final ExpulsionStatus expulsionStatus) {
        if (expulsionStatus.equals(ExpulsionStatus.NONE)) {
            return;
        }
        System.out.println("%s 대상자입니다.".formatted(ExpulsionStatusTextMaker.make(expulsionStatus)));
    }

    public void outputCheckExpulsionCrewsTitle() {
        System.out.println("제적 위험자 조회 결과");
    }

    public void outputCrewExpulsions(final List<CheckExpulsionResultDto> expulsionResults) {
        sortExpulsionResults(expulsionResults);
        for (CheckExpulsionResultDto expulsionResult : expulsionResults) {
            System.out.println("- %s: 결석 %d회, 지각 %d회 (%s)".formatted(expulsionResult.nickname(),
                    expulsionResult.absentCount(), expulsionResult.lateCount(),
                    ExpulsionStatusTextMaker.make(expulsionResult.expulsionStatus())));
        }
        System.out.println();
    }

    private static void sortExpulsionResults(final List<CheckExpulsionResultDto> expulsionResults) {
        expulsionResults.sort((o1, o2) -> {
            long firstAllAbsentCount = o1.totalAbsents();
            long secondAllAbsentCount = o2.totalAbsents();
            if (firstAllAbsentCount == secondAllAbsentCount) {
                return o1.nickname().compareTo(o2.nickname());
            }
            return Math.toIntExact(secondAllAbsentCount - firstAllAbsentCount);
        });
    }
}
