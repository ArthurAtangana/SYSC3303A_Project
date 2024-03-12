import Configuration.Config;
import Configuration.Configurator;
import Messaging.Messages.Events.FloorInputEvent;
import Messaging.Transceivers.Receivers.Receiver;
import Messaging.Transceivers.TransceiverDMAFactory;
import Messaging.Transceivers.TransceiverFactory;
import Subsystem.ElevatorSubsytem.Elevator;
import Subsystem.FloorSubsystem.DestinationDispatcher;
import Subsystem.FloorSubsystem.Floor;
import Subsystem.FloorSubsystem.Parser;
import Subsystem.SchedulerSubsystem.Scheduler;

import java.util.ArrayList;

/**
 * MainDMA initializes and maintains track of threads.
 *
 * @version Iteration-2
 */
public class MainDMA {
    /**
     * Iter2 Creation procedure
     * 1. Load numFloors, numElevators from config
     * 2. Create receivers (TODO: in the future, this will be part of subsystem creation)
     * 3. Create subsystems (TODO: in the future, will be part of step 2)
     * 4. Put subsystems in threads, and start them, in this order: Scheduler, Elevator, Floor
     * 5. Read inputs -> Start Dispatcher (as this will be part of floor later, floor should be the last subsystem)
     */
    public static void main(String[] args) {

        // 1. Configure system from JSON
        System.out.println("\n****** Configuring System ******\n");
        Config config = (new Configurator().getConfig());
        config.printConfig();
        int numFloors = config.getNumFloors();
        int numElevators = config.getNumElevators();

        // 2. Create receivers
        TransceiverFactory transceiverFactory = new TransceiverDMAFactory();

        Receiver schedulerReceiver = transceiverFactory.createServerReceiver();
        ArrayList<Receiver> elevatorReceivers = new ArrayList<>();
        for(int i=0; i < numElevators; i++){
            elevatorReceivers.add(transceiverFactory.createClientReceiver(i));
        }
        ArrayList<Receiver> floorReceivers = new ArrayList<>();
        for(int i=0; i < numFloors; i++){
            floorReceivers.add(transceiverFactory.createClientReceiver(i));
        }

        // 2. Start Scheduler threads
        Scheduler scheduler = new Scheduler(schedulerReceiver,
                transceiverFactory.createServerTransmitter(),
                transceiverFactory.createServerTransmitter());
        Thread schedulerThread = new Thread(scheduler);
        schedulerThread.start();

        // 3 + 4. Create then immediately start floors, and elevators
        for (int i = 0; i < numFloors; ++i) {
            new Thread(new Floor(i, floorReceivers.get(i),
                    transceiverFactory.createClientTransmitter())).start();
        }
        for (int i = 0; i < numElevators; ++i) {
            new Thread(new Elevator(i, elevatorReceivers.get(i),
                    transceiverFactory.createClientTransmitter())).start();
        }

        // 5. Instantiate Parser and parse input file to FloorInputEvents
        System.out.println("\n****** Generating System Input Events ******\n");
        Parser parser = new Parser();
        //String inputFilename = "res/input-file.txt";
        String inputFilename = "test/resources/test-input-file.txt";
        ArrayList<FloorInputEvent> inputEvents = parser.parse(inputFilename);

        // Start dispatcher (want all systems to be ready before sending events)
        System.out.println("\n****** Begin Real-Time System Operation ******\n");

        new Thread(new DestinationDispatcher(inputEvents,
                transceiverFactory.createClientTransmitter(),
                Floor.getFloorsTransmitter())).start();
    }
}

