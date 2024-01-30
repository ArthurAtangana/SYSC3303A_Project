import ElevatorSubsytem.Elevator;
import FloorSubsystem.Floor;
import SchedulerSubsystem.Scheduler;
import java.util.ArrayList;
/**
 * Main initializes and maintains track of threads.
 * Note: For iteration 1, number of floors and elevators are stored as fields.
 *
 * @version 1.0
 */
public class Main {
    private static int numFloors = 1;
    private static int numElevators = 1;
    private static ArrayList<Thread> floorThreads = new ArrayList<>();
    private static ArrayList<Thread> elevatorThreads = new ArrayList<>();

    public static void main(String[] args) {
        // Start floor, elevator, and scheduler threads
        for (int i = 0; i < numFloors; ++i) {
            Thread newFloor = new Thread(new Floor(), String.valueOf(i));
            floorThreads.add(newFloor);
            newFloor.start();
        }
        for (int i = 0; i < numElevators; ++i) {
            Thread newElevator = new Thread(new Elevator(), String.valueOf(i));
            elevatorThreads.add(newElevator);
            newElevator.start();
        }
        Thread scheduler = new Thread(new Scheduler(elevatorThreads, floorThreads), "Scheduler");
        scheduler.start();

        // Join floor, elevator, and scheduler threads
        for (Thread t: floorThreads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        for (Thread t: elevatorThreads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            scheduler.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

