package attendance.view;

import attendance.FeatureCommand;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputView {

    private final Scanner scanner = new Scanner(System.in);

    public FeatureCommand inputCommandWithDate(final LocalDate date) {
        System.out.printf("오늘은 %s입니다. 기능을 선택해 주세요.%n", ViewConstants.DATE_FORMATTER.format(date));
        System.out.println("1. 출석 확인");
        System.out.println("2. 출석 수정");
        System.out.println("3. 크루별 출석 기록 확인");
        System.out.println("4. 제적 위험자 확인");
        System.out.println("Q. 종료");
        return FeatureCommand.from(scanner.nextLine());
    }

    public String inputCrewNickname() {
        System.out.println("닉네임을 입력해 주세요.");
        return scanner.nextLine();
    }

    public LocalTime inputAttendanceTime() {
        System.out.println("등교 시간을 입력해 주세요.");
        try {
            return LocalTime.parse(scanner.nextLine(), ViewConstants.TIME_FORMATTER);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("시간을 올바르게 입력해 주세요.");
        }
    }

    public String inputCrewNicknameForModifyAttendance() {
        System.out.println("출석을 수정하려는 크루의 닉네임을 입력해 주세요.");
        return scanner.nextLine();
    }

    public LocalDate inputDayOfMonthForModifyAttendance() {
        System.out.println("수정하려는 날짜(일)를 입력해 주세요.");
        int dayOfWeekForModify = NumberParser.parse(scanner.nextLine());
        return LocalDate.now().withDayOfMonth(dayOfWeekForModify);
    }

    public LocalTime inputTimeToModify() {
        System.out.println("언제로 변경하겠습니까?");
        try {
            return LocalTime.parse(scanner.nextLine(), ViewConstants.TIME_FORMATTER);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("시간을 올바르게 입력해 주세요.");
        }
    }
}
