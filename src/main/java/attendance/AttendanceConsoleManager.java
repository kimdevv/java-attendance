package attendance;

import attendance.dto.CheckExpulsionResultDto;
import attendance.model.AttendanceStatusChecker;
import attendance.model.AttendanceStatusChecker.AttendanceStatus;
import attendance.model.Crew;
import attendance.model.Crews;
import attendance.model.ExpulsionStatus;
import attendance.view.FileLineReader;
import attendance.view.InputView;
import attendance.view.OutputView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AttendanceConsoleManager {

    public static final String ATTENDANCE_FILE_PATH = "src/main/resources/";
    public static final String ATTENDANCE_FILE_NAME = "attendances.csv";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static final int LATE_COUNT_PER_ABSENT = 3;

    private final InputView inputView;
    private final OutputView outputView;

    public AttendanceConsoleManager(final InputView inputView, final OutputView outputView) {
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void run() {
        Crews crews = initializeCrewsFromFile();
        while(true) {
            try {
                FeatureCommand featureCommand = inputView.inputCommandWithDate(LocalDate.now());
                branchByFeatureCommand(featureCommand, crews);
            } catch (IllegalArgumentException exception) {
                outputView.outputExceptionMessage(exception.getMessage());
            }
        }
    }

    private Crews initializeCrewsFromFile() {
        List<String> crewAttendanceTexts = FileLineReader.readAllLines(ATTENDANCE_FILE_PATH, ATTENDANCE_FILE_NAME);
        crewAttendanceTexts.removeFirst();
        List<Crew> crews = new ArrayList<>();
        for (String crewAttendanceText : crewAttendanceTexts) {
            String[] attendanceInformation = crewAttendanceText.split(",");
            Crew crew = crews.stream()
                    .filter(registeredCrew -> registeredCrew.getNickname().equalsIgnoreCase(attendanceInformation[0]))
                    .findFirst()
                    .orElse(Crew.generateWithAttendancesUntilToday(attendanceInformation[0]));
            crews.remove(crew);
            crew.addAttendance(LocalDateTime.parse(attendanceInformation[1], DATE_TIME_FORMATTER));
            crews.add(crew);
        }
        return new Crews(crews);
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
        Crew crew = crews.findByNickname(inputView.inputCrewNickname());
        if (crew.isAttendedToday()) {
            throw new IllegalArgumentException("오늘은 이미 출석하셨습니다. 출석 수정 기능을 이용해 주세요.");
        }
        LocalTime attendanceTime = inputView.inputAttendanceTime();
        LocalDate today = LocalDate.now();
        crew.addAttendance(today, attendanceTime);
        outputView.outputAttendanceInformation(today, attendanceTime, AttendanceStatusChecker.checkStatus(today, attendanceTime));
    }

    private void modifyAttendance(final Crews crews) {
        Crew crew = crews.findByNickname(inputView.inputCrewNicknameForModifyAttendance());
        LocalDate attendanceDate = inputView.inputDayOfMonthForModifyAttendance();
        LocalTime newAttendanceTime = inputView.inputTimeToModify();
        LocalTime originalAttendanceTime = crew.findAttendanceTimeInGivenDate(attendanceDate);
        outputView.outputModifyAttendanceResult(attendanceDate, originalAttendanceTime, newAttendanceTime);
    }

    private void checkCrewAttendance(final Crews crews) {
        Crew crew = crews.findByNickname(inputView.inputCrewNicknameForModifyAttendance());
        Map<LocalDate, LocalTime> crewAttendances = crew.getAttendances();
        Map<AttendanceStatus, Long> attendanceStatuses = AttendanceStatusChecker.checkStatuses(crewAttendances);
        ExpulsionStatus expulsionStatus = ExpulsionStatus.from(calculateTotalAbsentCounts(attendanceStatuses));
        outputView.outputCrewAttendances(crew, crewAttendances);
        outputView.outputAttendanceStatuses(attendanceStatuses);
        outputView.outputExpulsionStatus(expulsionStatus);
    }

    private int calculateTotalAbsentCounts(final Map<AttendanceStatus, Long> attendanceStatuses) {
        long absentCount = attendanceStatuses.get(AttendanceStatus.ABSENT);
        absentCount += attendanceStatuses.get(AttendanceStatus.LATE) / LATE_COUNT_PER_ABSENT;
        return Math.toIntExact(absentCount);
    }

    private void checkAllExpulsionCrews(final Crews crews) {
        outputView.outputCheckExpulsionCrewsTitle();
        List<CheckExpulsionResultDto> expulsionResults = new ArrayList<>();
        for (Crew crew : crews.getCrews()) {
            Map<AttendanceStatus, Long> attendanceStatuses = AttendanceStatusChecker.checkStatuses(crew.getAttendances());
            ExpulsionStatus expulsionStatus = ExpulsionStatus.from(calculateTotalAbsentCounts(attendanceStatuses));
            if (expulsionStatus == ExpulsionStatus.NONE) {
                continue;
            }
            int totalAbsentCount = calculateTotalAbsentCounts(attendanceStatuses);
            expulsionResults.add(new CheckExpulsionResultDto(crew.getNickname(),
                    attendanceStatuses.get(AttendanceStatus.ABSENT), attendanceStatuses.get(AttendanceStatus.LATE), totalAbsentCount, expulsionStatus));
        }
        outputView.outputCrewExpulsions(expulsionResults);
    }
}
