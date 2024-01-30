import ElevatorSubsytem.Elevator;
import FloorSubsystem.Floor;
import SchedulerSubsystem.Scheduler;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        Thread FloorSubsystem = new Thread(new Floor());
        Thread ElevatorSubsystem = new Thread(new Elevator());
        Thread SchedulerSubsystem = new Thread(new Scheduler());

        FloorSubsystem.start();
        ElevatorSubsystem.start();
        SchedulerSubsystem.start();
    }
}