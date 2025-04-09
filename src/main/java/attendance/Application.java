package attendance;

import attendance.view.InputView;
import attendance.view.OutputView;

public class Application {

    public static void main(String[] args) {
        AttendanceConsoleManager attendanceConsoleManager = new AttendanceConsoleManager(new InputView(), new OutputView());
        attendanceConsoleManager.run();
    }
}
