import Configuration.Config;
import Configuration.Configurator;
import Messaging.Messages.Events.FloorInputEvent;
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
     * All subsystems combined start procedure (using DMA):
     * 1. Load numFloors, numElevators from config
     * 2. Create transceiver factory
     * 3. Create and start subsystems, (start Scheduler first).
     * 4. Parse input
     * 5. Put input into dispatcher and start it
     */
    public static void main(String[] args) {
        // 1. Configure system from JSON
        System.out.println("\n****** Configuring System ******\n");
        Config config = (new Configurator().getConfig());
        config.printConfig();

        // 2. Create factory
        TransceiverFactory dmaFactory = new TransceiverDMAFactory();

        // 3. Create and start Subsystem threads
        Scheduler scheduler = new Scheduler(dmaFactory.createServerReceiver(),
                dmaFactory.createServerTransmitter(),
                dmaFactory.createServerTransmitter());
        Thread schedulerThread = new Thread(scheduler);
        schedulerThread.start();

        // Floor and elevator thread creation
        for (int i = 0; i < config.getNumFloors(); ++i) {
            new Thread(new Floor(i, dmaFactory.createClientReceiver(i),
                    dmaFactory.createClientTransmitter())).start();
        }
        for (int i = 0; i < config.getNumElevators(); ++i) {
            new Thread(new Elevator(i, dmaFactory.createClientReceiver(i),
                    dmaFactory.createClientTransmitter())).start();
        }

        // 4. Instantiate Parser and parse input file to FloorInputEvents
        System.out.println("\n****** Generating System Input Events ******\n");
        Parser parser = new Parser();
        String inputFilename = "test/resources/test-input-file.txt";
        ArrayList<FloorInputEvent> inputEvents = parser.parse(inputFilename);

        // 5. Start dispatcher (want all systems to be ready before sending events)
        System.out.println("\n****** Begin Real-Time System Operation ******\n");

        new Thread(new DestinationDispatcher(inputEvents,
                dmaFactory.createClientTransmitter(),
                Floor.getFloorsTransmitter())).start();
    }
}

