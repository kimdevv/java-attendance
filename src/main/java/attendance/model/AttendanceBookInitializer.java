package attendance.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AttendanceBookInitializer {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static final String CREW_DATETIME_DELIMITER = ",";

    public AttendanceBook initialize(final List<String> crewAttendanceTexts) {
        Map<Crew, List<AttendanceDateTime>> parsedCrewAttendances = parseTexts(crewAttendanceTexts);
        Map<Crew, Attendances> crewAttendances = parsedCrewAttendances.keySet().stream()
                .collect(Collectors.toMap(key -> key, key -> new Attendances(parsedCrewAttendances.get(key))));
        return new AttendanceBook(crewAttendances);
    }

    private Map<Crew, List<AttendanceDateTime>> parseTexts(final List<String> crewAttendanceTexts) {
        Map<Crew, List<AttendanceDateTime>> crewAttendances = crewAttendanceTexts.stream()
                .map(attendanceText -> attendanceText.split(CREW_DATETIME_DELIMITER))
                .collect(Collectors.toMap(text -> new Crew(text[0]),
                        text -> new ArrayList<>(List.of(new AttendanceDateTime(parseDateTime(text[1])))),
                        (existingList, newList) -> mergeTwoLists(existingList, newList)));
        return crewAttendances;
    }

    private LocalDateTime parseDateTime(final String localDateTimeText) {
        return LocalDateTime.parse(localDateTimeText, DATE_TIME_FORMATTER);
    }

    private List<AttendanceDateTime> mergeTwoLists(final List<AttendanceDateTime> existingList,
                                                   final List<AttendanceDateTime> newList) {
        existingList.addAll(newList);
        return existingList;
    }
}
