package attendance;

import attendance.dto.CheckExpulsionResultDto;
import attendance.model.AttendanceStatusChecker;
import attendance.model.AttendanceStatusChecker.AttendanceStatus;
import attendance.model.Crew;
import attendance.model.Crews;
import attendance.model.CrewsInitializer;
import attendance.model.ExpulsionStatus;
import attendance.view.InputView;
import attendance.view.OutputView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AttendanceConsoleManager {

    private final InputView inputView;
    private final OutputView outputView;

    public AttendanceConsoleManager(final InputView inputView, final OutputView outputView) {
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void run() {
        Crews crews = CrewsInitializer.initializeFromAttendanceFile();
        while(true) {
            try {
                FeatureCommand featureCommand = inputView.inputCommandWithDate(LocalDate.now());
                branchByFeatureCommand(featureCommand, crews);
            } catch (IllegalArgumentException exception) {
                outputView.outputExceptionMessage(exception.getMessage());
            }
        }
    }

    private void branchByFeatureCommand(final FeatureCommand featureCommand, final Crews crews) {
        if (featureCommand.equals(FeatureCommand.ATTENDANCE_CONFIRMATION)) {
            confirmAttendance(crews);
        }
        if (featureCommand.equals(FeatureCommand.ATTENDANCE_MODIFICATION)) {
            modifyAttendance(crews);
        }
        if (featureCommand.equals(FeatureCommand.CREW_ATTENDANCE_CHECK)) {
            checkCrewAttendance(crews);
        }
        if (featureCommand.equals(FeatureCommand.EXPULSION_CREW_CHECK)) {
            checkAllExpulsionCrews(crews);
        }
        if (featureCommand.equals(FeatureCommand.QUIT)) {
            System.exit(0);
        }
    }

    private void confirmAttendance(final Crews crews) {
        String nickname = inputView.inputCrewNickname();
        crews.validateCanAttendToday(nickname);
        LocalTime attendanceTime = inputView.inputAttendanceTime();
        LocalDate today = LocalDate.now();
        crews.addCrewAttendance(nickname, today, attendanceTime);
        outputView.outputAttendanceInformation(today, attendanceTime, AttendanceStatusChecker.checkStatus(today, attendanceTime));
    }

    private void modifyAttendance(final Crews crews) {
        String nickname = inputView.inputCrewNicknameForModifyAttendance();
        LocalDate attendanceDate = inputView.inputDayOfMonthForModifyAttendance();
        LocalTime newAttendanceTime = inputView.inputTimeToModify();
        LocalTime originalAttendanceTime = crews.findCrewAttendanceTimeAt(nickname, attendanceDate);
        crews.modifyCrewAttendanceTime(nickname, attendanceDate, newAttendanceTime);
        outputView.outputModifyAttendanceResult(attendanceDate, originalAttendanceTime, newAttendanceTime);
    }

    private void checkCrewAttendance(final Crews crews) {
        String nickname = inputView.inputCrewNickname();
        Map<LocalDate, LocalTime> crewAttendances = crews.getCrewAttendances(nickname);
        Map<AttendanceStatus, Long> attendanceStatuses = crews.calculateCrewAttendanceStatuses(nickname);
        ExpulsionStatus expulsionStatus = crews.calculateCrewExpulsionStatus(nickname);
        outputView.outputCrewAttendances(nickname, crewAttendances);
        outputView.outputAttendanceStatuses(attendanceStatuses);
        outputView.outputExpulsionStatus(expulsionStatus);
    }

    private void checkAllExpulsionCrews(final Crews crews) {
        outputView.outputCheckExpulsionCrewsTitle();
        List<Crew> expulsionCrews = crews.findExpulsionCrews();
        List<CheckExpulsionResultDto> expulsionResults = new ArrayList<>();
        for (Crew expulsionCrew : expulsionCrews) {
            expulsionResults.add(generateExpulsionResult(expulsionCrew));
        }
        outputView.outputCrewExpulsions(expulsionResults);
    }

    private CheckExpulsionResultDto generateExpulsionResult(Crew expulsionCrew) {
        Map<AttendanceStatus, Long> attendanceStatuses = expulsionCrew.calculateAttendanceStatuses();
        ExpulsionStatus expulsionStatus = expulsionCrew.calculateExpulsionStatus();
        int totalAbsentCount = expulsionCrew.calculateTotalAbsentCounts();
        return new CheckExpulsionResultDto(expulsionCrew.getNickname(),
                attendanceStatuses.get(AttendanceStatus.ABSENT), attendanceStatuses.get(AttendanceStatus.LATE), totalAbsentCount, expulsionStatus);
    }
}
