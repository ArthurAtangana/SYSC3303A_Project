import Configuration.Configurator;
import Configuration.Config;
import ElevatorSubsytem.Elevator;
import FloorSubsystem.DestinationDispatcher;
import FloorSubsystem.Floor;
import FloorSubsystem.Parser;
import Messaging.Messages.Events.FloorInputEvent;
import Messaging.Transceivers.Receivers.ReceiverDMA;
import Messaging.Transceivers.Transmitters.Transmitter;
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

    /**
     * Iter2 Creation procedure
     * 1. Load numFloors, numElevators from config
     * 2. Create receivers (TODO: in the future, this will be part of subsystem creation)
     * 3. Bind transmitters
     * 4. Create subsystems (TODO: in the future, will be part of step 2)
     * 5. Put subsystems in threads, and start them, in this order: Scheduler, Floor, Elevator
     * 6. Read inputs -> Start Dispatcher
     */
    public static void main(String[] args) {

        // 1. Configure system from JSON
        System.out.println("\n****** Configuring System ******\n");
        String jsonFilename = "res/system-config-00.json";
        System.out.println("Reading system configuration from \"" + jsonFilename +"\"...");
        Config config = (new Configurator(jsonFilename).getConfig());
        config.printConfig();
        int numFloors = config.getNumFloors();
        int numElevators = config.getNumElevators();

        // 2. Create receivers
        ReceiverDMA schedulerReceiver = new ReceiverDMA(0); // Scheduler is unique, no need for keys/commands.
        ArrayList<ReceiverDMA> elevatorReceivers = new ArrayList<>();
        for(int i=0; i < numElevators; i++){
            elevatorReceivers.add(new ReceiverDMA(i));
        }
        ArrayList<ReceiverDMA> floorReceivers = new ArrayList<>();
        for(int i=0; i < numFloors; i++){
            floorReceivers.add(new ReceiverDMA(i));
        }

        // 3. Bind Transmitters (composes with receivers)
        TransmitterDMA toSchedulerTransmitter = new TransmitterDMA();
        TransmitterDMA toElevatorTransmitter = new TransmitterDMA();
        TransmitterDMA toFloorsTransmitter = new TransmitterDMA();

        toSchedulerTransmitter.addReceiver(schedulerReceiver);
        for (ReceiverDMA floor : floorReceivers) {
            toFloorsTransmitter.addReceiver(floor);
        }
        for (ReceiverDMA elevator : elevatorReceivers) {
            toElevatorTransmitter.addReceiver(elevator);
        }

        // 4+5. Start Scheduler threads
        Scheduler scheduler = new Scheduler(schedulerReceiver, toFloorsTransmitter, toElevatorTransmitter);
        Thread schedulerThread = new Thread(scheduler);
        schedulerThread.start();

        // 4+5. Start floors, and elevators
        for (int i = 0; i < numFloors; ++i) {
            Thread newFloor = new Thread(new Floor(i, floorReceivers.get(i)));
            floorThreads.add(newFloor);
            newFloor.start();
        }
        for (int i = 0; i < numElevators; ++i) {
            Thread newElevator = new Thread(new Elevator(i, elevatorReceivers.get(i), toSchedulerTransmitter));
            elevatorThreads.add(newElevator);
            newElevator.start();
        }


        // 6. Instantiate Parser and parse input file to FloorInputEvents
        System.out.println("\n****** Generating System Input Events ******\n");
        Parser parser = new Parser();
        //String inputFilename = "res/input-file.txt";
        String inputFilename = "test/resources/test-input-file.txt";
        ArrayList<FloorInputEvent> inputEvents = parser.parse(inputFilename);

        // Start dispatcher (want all systems to be ready before sending events)
        System.out.println("\n****** Begin Real-Time System Operation ******\n");
        new Thread(new DestinationDispatcher(inputEvents, toSchedulerTransmitter, toFloorsTransmitter)).start();
    }
}

