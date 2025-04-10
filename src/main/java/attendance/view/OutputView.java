package attendance.view;

import attendance.dto.CheckExpulsionResultDto;
import attendance.model.AttendanceStatusChecker;
import attendance.model.AttendanceStatusChecker.AttendanceStatus;
import attendance.model.Attendances;
import attendance.model.ExpulsionStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public class OutputView {

    public void outputExceptionMessage(final String exceptionMessage) {
        System.out.println("[ERROR] " + exceptionMessage);
        System.out.println();
    }

    public void outputAttendanceInformation(final LocalDate attendanceDate, final LocalTime attendanceTime, final AttendanceStatus attendanceStatus) {
        outputAttendanceInformation(LocalDateTime.of(attendanceDate, attendanceTime), attendanceStatus);
    }

    public void outputAttendanceInformation(final LocalDateTime attendanceDateTime, final AttendanceStatus attendanceStatus) {
        System.out.printf("%s", ViewConstants.DATE_FORMATTER.format(attendanceDateTime));
        if (attendanceDateTime.toLocalTime() == Attendances.ABSENT_TIME) {
            System.out.printf(" --:-- (결석)%n");
            return;
        }
        String attendanceStatusText = AttendanceStatusTextMaker.make(attendanceStatus);
        System.out.printf(" %s (%s)%n", ViewConstants.TIME_FORMATTER.format(attendanceDateTime), attendanceStatusText);
    }

    public void outputModifyAttendanceResult(final LocalDate attendanceDate, final LocalTime originalAttendanceTime, final LocalTime newAttendanceTime) {
        String originalAttendanceStatusText = AttendanceStatusTextMaker.make(AttendanceStatusChecker.checkStatus(attendanceDate, originalAttendanceTime));
        String newAttendanceStatusText = AttendanceStatusTextMaker.make(AttendanceStatusChecker.checkStatus(attendanceDate, newAttendanceTime));
        System.out.printf("%s %s (%s) -> %s (%s) 수정 완료!%n%n",
                ViewConstants.DATE_FORMATTER.format(attendanceDate),
                ViewConstants.TIME_FORMATTER.format(originalAttendanceTime), originalAttendanceStatusText,
                ViewConstants.TIME_FORMATTER.format(newAttendanceTime), newAttendanceStatusText);
    }

    public void outputCrewAttendances(final String crewNickname, final Map<LocalDate, LocalTime> crewAttendances) {
        System.out.printf("이번 달 %s의 출석 기록입니다.\n%n", crewNickname);
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
            System.out.printf("%s: %d회%n", attendanceStatusText, statusCount);
        }
        System.out.println();
    }

    public void outputExpulsionStatus(final ExpulsionStatus expulsionStatus) {
        if (expulsionStatus.equals(ExpulsionStatus.NONE)) {
            return;
        }
        System.out.printf("%s 대상자입니다.%n", ExpulsionStatusTextMaker.make(expulsionStatus));
    }

    public void outputCheckExpulsionCrewsTitle() {
        System.out.println("제적 위험자 조회 결과");
    }

    public void outputCrewExpulsions(final List<CheckExpulsionResultDto> expulsionResults) {
        sortExpulsionResults(expulsionResults);
        for (CheckExpulsionResultDto expulsionResult : expulsionResults) {
            System.out.printf("- %s: 결석 %d회, 지각 %d회 (%s)%n", expulsionResult.nickname(),
                    expulsionResult.absentCount(), expulsionResult.lateCount(),
                    ExpulsionStatusTextMaker.make(expulsionResult.expulsionStatus()));
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
