import ElevatorSubsytem.Elevator;
import FloorSubsystem.DestinationDispatcher;
import FloorSubsystem.Floor;
import FloorSubsystem.Parser;
import Networking.Events.FloorInputEvent;
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
    // Note: our system starts counting floors at 0 :)
    private static final int NUM_FLOORS = 8;
    private static final int NUM_ELEVATORS = 1;
    private static final ArrayList<Thread> floorThreads = new ArrayList<>();
    private static final ArrayList<Thread> elevatorThreads = new ArrayList<>();

    public static void main(String[] args) {
        // Create receivers
        DMA_Receiver schedulerReceiver = new DMA_Receiver();
        DMA_Receiver elevatorReceiver = new DMA_Receiver();
        ArrayList<DMA_Receiver> floorReceivers = new ArrayList<>();
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
        for (int i = 0; i < NUM_FLOORS; ++i) {
            Thread newFloor = new Thread(new Floor(i, floorReceivers.get(i)));
            floorThreads.add(newFloor);
            newFloor.start();
        }
        for (int i = 0; i < NUM_ELEVATORS; ++i) {
            Thread newElevator = new Thread(new Elevator(elevatorReceiver, toSchedulerTransmitter));
            elevatorThreads.add(newElevator);
            newElevator.start();
        }
        schedulerThread.start();

        // Instantiate Parser and parse input file to FloorInputEvents
        System.out.println("\n****** Generating System Input Events ******\n");
        Parser parser = new Parser();
        ArrayList<FloorInputEvent> inputEvents = parser.parse("input-file.txt");

        // Start dispatcher (want all systems to be ready before sending events)
        System.out.println("\n****** Begin Real-Time System Operation ******\n");
        new Thread(new DestinationDispatcher(inputEvents, toSchedulerTransmitter)).start();

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

