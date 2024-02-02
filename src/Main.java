import ElevatorSubsytem.Elevator;
import FloorSubsystem.Floor;
import Networking.Receivers.DMA_Receiver;
import Networking.Transmitters.DMA_Transmitter;
import SchedulerSubsystem.Scheduler;
import java.util.ArrayList;
/**
 * Main initializes and maintains track of threads.
 * Note: For iteration 1, number of floors and elevators are stored as fields.
 *
 * @version 1.0
 */
public class Main {
    private static final int NUM_FLOORS = 1;
    private static final int numElevators = 1;
    private static final ArrayList<Thread> floorThreads = new ArrayList<>();
    private static final ArrayList<Thread> elevatorThreads = new ArrayList<>();

    public static void main(String[] args) {
        // Create receivers
        DMA_Receiver schedulerReceiver = new DMA_Receiver();
        DMA_Receiver elevatorReceiver = new DMA_Receiver();
        ArrayList<DMA_Receiver> floorReceivers = new ArrayList<DMA_Receiver>();
        for(int i=0; i < NUM_FLOORS; i++){
            floorReceivers.add(new DMA_Receiver());
        }

        // Create Transmitters (composes with receivers)
        DMA_Transmitter toSchedulerTransmitter = new DMA_Transmitter(schedulerReceiver);
        DMA_Transmitter toElevatorTransmitter = new DMA_Transmitter(elevatorReceiver);
        DMA_Transmitter toFloorsTransmitter = new DMA_Transmitter(floorReceivers);

        // Start floor, elevator, and scheduler threads
        Scheduler scheduler = new Scheduler(schedulerReceiver, toFloorsTransmitter, toElevatorTransmitter);
        Thread schedulerThread = new Thread(scheduler);
        for (int i = 1; i < NUM_FLOORS + 1; ++i) {
            Thread newFloor = new Thread(new Floor(i, floorReceivers.get(i)));
            floorThreads.add(newFloor);
            newFloor.start();
        }
        for (int i = 1; i < numElevators + 1; ++i) {
            Thread newElevator = new Thread(new Elevator(elevatorReceiver, toSchedulerTransmitter));
            elevatorThreads.add(newElevator);
            newElevator.start();
        }
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
}

