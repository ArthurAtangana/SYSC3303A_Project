import Configuration.Configurator;
import Configuration.Config;
import ElevatorSubsytem.Elevator;
import FloorSubsystem.DestinationDispatcher;
import FloorSubsystem.Floor;
import FloorSubsystem.Parser;
import Messaging.Messages.Events.FloorInputEvent;
import Messaging.Transceivers.Receivers.ReceiverDMA;
import Messaging.Transceivers.Transmitters.TransmitterDMA;
import SchedulerSubsystem.Scheduler;

import java.util.ArrayList;

/**
 * Main initializes and maintains track of threads.
 *
 * @version Iteration-2
 */
public class Main {
    // Note: our system starts counting floors at 0 :)
    private static final ArrayList<Thread> floorThreads = new ArrayList<>();
    private static final ArrayList<Thread> elevatorThreads = new ArrayList<>();

    public static void main(String[] args) {

        // Configure system from JSON
        System.out.println("\n****** Configuring System ******\n");
        String jsonFilename = "res/system-config-00.json";
        System.out.println("Reading system configuration from \"" + jsonFilename +"\"...");
        Config config = (new Configurator(jsonFilename).getConfig());
        config.printConfig();
        int numFloors = config.getNumFloors();
        int numElevators = config.getNumElevators();

        // Create receivers
        ReceiverDMA schedulerReceiver = new ReceiverDMA(); // Scheduler is unique, no need for keys/commands.
        ReceiverDMA elevatorReceiver = new ReceiverDMA(0);
        ArrayList<ReceiverDMA> floorReceivers = new ArrayList<>();
        for(int i=0; i < numFloors; i++){
            floorReceivers.add(new ReceiverDMA(i));
        }

        // Create Transmitters (composes with receivers)
        TransmitterDMA toSchedulerTransmitter = new TransmitterDMA(schedulerReceiver);
        TransmitterDMA toElevatorTransmitter = new TransmitterDMA(elevatorReceiver);
        TransmitterDMA toFloorsTransmitter = new TransmitterDMA(floorReceivers);

        // Start floor, elevator, and scheduler threads
        Scheduler scheduler = new Scheduler(schedulerReceiver, toFloorsTransmitter, toElevatorTransmitter);
        Thread schedulerThread = new Thread(scheduler);

        schedulerThread.start();

        for (int i = 0; i < numFloors; ++i) {
            Thread newFloor = new Thread(new Floor(i, floorReceivers.get(i)));
            floorThreads.add(newFloor);
            newFloor.start();
        }

        for (int i = 0; i < numElevators; ++i) {
            Thread newElevator = new Thread(new Elevator(i, elevatorReceiver, toSchedulerTransmitter));
            elevatorThreads.add(newElevator);
            newElevator.start();
        }


        // Instantiate Parser and parse input file to FloorInputEvents
        System.out.println("\n****** Generating System Input Events ******\n");
        Parser parser = new Parser();
        //String inputFilename = "res/input-file.txt";
        String inputFilename = "test/resources/test-input-file.txt";
        ArrayList<FloorInputEvent> inputEvents = parser.parse(inputFilename);

        // Start dispatcher (want all systems to be ready before sending events)
        System.out.println("\n****** Begin Real-Time System Operation ******\n");
        new Thread(new DestinationDispatcher(inputEvents, toSchedulerTransmitter, toFloorsTransmitter)).start();

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

