import Configuration.Config;
import Configuration.Configurator;
import Messaging.Events.FloorInputEvent;
import Subsystem.ElevatorSubsytem.StateMachine.ElevatorContext;
import Subsystem.FloorSubsystem.DestinationDispatcher;
import Subsystem.FloorSubsystem.Parser;
import Subsystem.FloorSubsystem.StateMachine.FloorContext;
import Subsystem.SchedulerSubsystem.SchedulerContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Main initializes and maintains track of threads.
 * Note: For iteration 1, number of floors and elevators are stored as fields.
 *
 * @version 1.0
 */
public class Main {
    /**
     * Iter2 Creation procedure
     * 1. Load numFloors, numElevators from config
     * 2. Create subsystems
     * 3. Bind transmitters
     * 4. Put subsystems in threads, and start them, in this order: Scheduler, Floor, Elevator
     * 5. Read inputs -> Start Dispatcher
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

        // 2. Create subsystems
        SchedulerContext schedulerContext = new SchedulerContext();
        List<FloorContext> floorContexts = new ArrayList<>();
        List<ElevatorContext> elevatorContexts = new ArrayList<>();
        for(int i=0; i < numFloors; i++){
            floorContexts.add(new FloorContext());
        }
        for (int i = 0; i < numElevators; i++) {
            elevatorContexts.add(new ElevatorContext());
        }

        // 3. Bind transmitters
        for (FloorContext floor : floorContexts) {
            floor.bindToScheduler(schedulerContext);
        }
        for (ElevatorContext elevator : elevatorContexts) {
            elevator.bindToScheduler(schedulerContext);
        }

        // 4. Start subsystem threads
        new Thread(schedulerContext).start();
        for (int i = 0; i < numFloors; ++i) {
            new Thread(floorContexts.get(i)).start();
        }
        for (int i = 0; i < numElevators; ++i) {
            new Thread(elevatorContexts.get(i)).start();
        }

        // Instantiate Parser and parse input file to FloorInputEvents
        System.out.println("\n****** Generating System Input Events ******\n");
        Parser parser = new Parser();
        String inputFilename = "res/input-file.txt";
        ArrayList<FloorInputEvent> inputEvents = parser.parse(inputFilename);

        // Start dispatcher (want all systems to be ready before sending events)
        DestinationDispatcher dispatcher = new DestinationDispatcher(inputEvents);
        dispatcher.bindToScheduler(schedulerContext);

        System.out.println("\n****** Begin Real-Time System Operation ******\n");
        new Thread(dispatcher).start();
    }
}

