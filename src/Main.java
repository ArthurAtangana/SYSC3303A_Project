import ElevatorSubsytem.Elevator;
import FloorSubsystem.Floor;
import Networking.Receivers.DMA_Receiver;
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
    private static DMA_Receiver schedulerReceiver;

    /*
    TODO:
    - create main models
    - bind transmitters to receivers
    - start threads.
     */
    public static void main(String[] args) {
        // Start floor, elevator, and scheduler threads
        initializeReceivers();
        Scheduler scheduler = new Scheduler();
        Thread schedulerThread = new Thread(scheduler);
        for (int i = 1; i < numFloors + 1; ++i) {
            Thread newFloor = new Thread(new Floor(i, schedulerReceiver));
            floorThreads.add(newFloor);
            newFloor.start();
        }
        for (int i = 1; i < numElevators + 1; ++i) {
            Thread newElevator = new Thread(new Elevator(scheduler));
            elevatorThreads.add(newElevator);
            newElevator.start();
        }
        scheduler.setFloors(floorThreads);
        scheduler.setElevators(elevatorThreads);
        schedulerThread.start();

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
            schedulerThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Initialize receivers for all subsystems
     */
    private static void initializeReceivers() {
        schedulerReceiver = new DMA_Receiver();
    }
}

