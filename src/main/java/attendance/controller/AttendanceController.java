package attendance.controller;

import attendance.domain.*;
import attendance.domain.AttendanceStatusChecker.AttendanceStatus;
import attendance.dto.CheckExpulsionResultDto;
import attendance.view.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AttendanceController {

    public static final String ATTENDANCE_FILE_PATH = "src/main/resources/";
    public static final String ATTENDANCE_FILE_NAME = "attendances.csv";
    private final GeneralView generalView;
    private final AttendanceConfirmView attendanceConfirmView;
    private final AttendanceModifyView attendanceModifyView;
    private final CrewAttendanceCheckView crewAttendanceCheckView;
    private final CheckAllExpulsionCrewView checkAllExpulsionCrewView;
    private final AttendanceBook attendanceBook;

    public AttendanceController(final GeneralView generalView,
                                final AttendanceConfirmView attendanceConfirmView,
                                final AttendanceModifyView attendanceModifyView,
                                final CrewAttendanceCheckView crewAttendanceCheckView,
                                final CheckAllExpulsionCrewView checkAllExpulsionCrewView) {
        this.generalView = generalView;
        this.attendanceConfirmView = attendanceConfirmView;
        this.attendanceModifyView = attendanceModifyView;
        this.crewAttendanceCheckView = crewAttendanceCheckView;
        this.checkAllExpulsionCrewView = checkAllExpulsionCrewView;
        this.attendanceBook = initializeAttendanceBook();
    }

    public void run() {
        while(true) {
            try {
                FeatureCommand featureCommand = generalView.readCommandWithToday(LocalDate.now());
                branchByFeatureCommand(featureCommand);
            } catch (IllegalArgumentException exception) {
                generalView.printExceptionMessage(exception.getMessage());
            }
        }
    }

    private AttendanceBook initializeAttendanceBook() {
        List<String> crewAttendanceTexts = FileLineReader.readAllLines(ATTENDANCE_FILE_PATH, ATTENDANCE_FILE_NAME);
        crewAttendanceTexts.removeFirst();
        AttendanceBookInitializer attendanceBookInitializer = new AttendanceBookInitializer();
        return attendanceBookInitializer.initialize(crewAttendanceTexts);
    }

    private void branchByFeatureCommand(final FeatureCommand featureCommand) {
        if (featureCommand.equals(FeatureCommand.ATTENDANCE_CONFIRMATION)) {
            confirmAttendance();
        }
        if (featureCommand.equals(FeatureCommand.ATTENDANCE_MODIFICATION)) {
            modifyAttendance();
        }
        if (featureCommand.equals(FeatureCommand.CREW_ATTENDANCE_CHECK)) {
            checkCrewAttendance();
        }
        if (featureCommand.equals(FeatureCommand.EXPULSION_CREW_CHECK)) {
            checkAllExpulsionCrews();
        }
        if (featureCommand.equals(FeatureCommand.QUIT)) {
            System.exit(0);
        }
    }

    private void confirmAttendance() {
        Crew crew = getCrewIfExistInAttendanceBook(attendanceConfirmView.readCrewNickname());
        LocalDateTime dateTime = attendanceConfirmView.readAttendanceTime();
        AttendanceDateTime attendanceDateTime = new AttendanceDateTime(dateTime);
        attendanceBook.saveAttendanceDateTime(crew, attendanceDateTime);
        AttendanceStatus attendanceStatus = AttendanceStatusChecker.checkStatus(attendanceDateTime);
        attendanceConfirmView.printAttendanceResult(attendanceDateTime, attendanceStatus);
    }

    private Crew getCrewIfExistInAttendanceBook(final String nickname) {
        Crew crew = new Crew(nickname);
        attendanceBook.validateRegisteredCrew(crew);
        return crew;
    }

    private void modifyAttendance() {
        Crew crew = getCrewIfExistInAttendanceBook(attendanceModifyView.readCrewNickname());
        int dayToModify = attendanceModifyView.readDayToModify();
        AttendanceDateTime originalDateTime = attendanceBook.findAttendanceDateTimeByCrewAndDate(crew, LocalDate.now().withDayOfMonth(dayToModify));
        LocalTime newTime = attendanceModifyView.readTimeToModify();
        AttendanceDateTime newDateTime = attendanceBook.changeCrewAttendanceTime(crew, originalDateTime, newTime);
        attendanceModifyView.printAttendanceModifyResult(originalDateTime, newDateTime);
    }

    private void checkCrewAttendance() {
        Crew crew = getCrewIfExistInAttendanceBook(crewAttendanceCheckView.readCrewNickname());
        List<AttendanceDateTime> crewAttendanceDateTimes = attendanceBook.findCrewAttendancesThisMonth(crew);
        Map<AttendanceStatus, Long> attendanceStatuses = AttendanceStatusChecker.checkStatuses(crewAttendanceDateTimes);
        ExpulsionStatus expulsionStatus = ExpulsionStatus.from(AttendanceStatusChecker.calculateAllAbsent(crewAttendanceDateTimes));
        crewAttendanceCheckView.printCrewAttendances(crew, crewAttendanceDateTimes);
        crewAttendanceCheckView.printAttendanceStatuses(attendanceStatuses);
        crewAttendanceCheckView.printExpulsionStatus(expulsionStatus);
    }

    private void checkAllExpulsionCrews() {
        checkAllExpulsionCrewView.printTitle();
        List<CheckExpulsionResultDto> expulsionResults = new ArrayList<>();
        Set<Crew> allCrews = attendanceBook.getAllCrews();
        for (Crew crew : allCrews) {
            List<AttendanceDateTime> attendancesThisMonth = attendanceBook.findCrewAttendancesThisMonth(crew);
            long allAbsents = AttendanceStatusChecker.calculateAllAbsent(attendancesThisMonth);
            ExpulsionStatus expulsionStatus = ExpulsionStatus.from(allAbsents);
            if (ExpulsionStatus.isExpulsionCrew(expulsionStatus)) {
                Map<AttendanceStatus, Long> attendanceStatuses = AttendanceStatusChecker.checkStatuses(attendancesThisMonth);
                expulsionResults.add(new CheckExpulsionResultDto(crew.getNickname(), attendanceStatuses.get(AttendanceStatus.ABSENT),
                        attendanceStatuses.get(AttendanceStatus.LATE), allAbsents, expulsionStatus));
            }
        }
        checkAllExpulsionCrewView.printCrewExpulsions(expulsionResults);
    }
}
