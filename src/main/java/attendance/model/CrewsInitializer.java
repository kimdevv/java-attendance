package attendance.model;

import attendance.view.FileLineReader;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CrewsInitializer {

    public static final String ATTENDANCE_FILE_PATH = "src/main/resources/";
    public static final String ATTENDANCE_FILE_NAME = "attendances.csv";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static final String ATTENDANCE_FILE_DELIMITER = ",";

    public static final int CREW_NICKNAME_INDEX = 0;
    public static final int DATE_TIME_INDEX = 1;

    public static Crews initializeFromAttendanceFile() {
        List<String> crewAttendanceTexts = readFirstLineRemovedAttendanceFile();
        List<Crew> crews = new ArrayList<>();
        for (String crewAttendanceText : crewAttendanceTexts) {
            String[] attendanceInformation = crewAttendanceText.split(ATTENDANCE_FILE_DELIMITER);
            Crew crew = findCrewOrGenerateNew(crews, attendanceInformation[CREW_NICKNAME_INDEX]);
            crew.addAttendance(LocalDateTime.parse(attendanceInformation[DATE_TIME_INDEX], DATE_TIME_FORMATTER));
            registerCrew(crews, crew);
        }
        return new Crews(crews);
    }

    private static List<String> readFirstLineRemovedAttendanceFile() {
        List<String> crewAttendanceTexts = FileLineReader.readAllLines(ATTENDANCE_FILE_PATH, ATTENDANCE_FILE_NAME);
        crewAttendanceTexts.removeFirst();
        return crewAttendanceTexts;
    }

    private static Crew findCrewOrGenerateNew(final List<Crew> crews, final String crewNickname) {
        return crews.stream()
                .filter(registeredCrew -> registeredCrew.getNickname().equalsIgnoreCase(crewNickname))
                .findFirst()
                .orElse(Crew.generateWithAttendancesUntilToday(crewNickname));
    }

    private static void registerCrew(final List<Crew> crews, final Crew crew) {
        crews.remove(crew);
        crews.add(crew);
    }
}
