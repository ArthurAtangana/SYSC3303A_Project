package Subsystem.SchedulerSubsystem;

import Configuration.Config;
import Logging.Logger;
import Messaging.Messages.Commands.MoveElevatorCommand;
import Messaging.Messages.Commands.SystemCommand;
import Messaging.Messages.Direction;
import Messaging.Messages.Events.DestinationEvent;
import Messaging.Messages.Events.ElevatorStateEvent;
import Messaging.Messages.Events.FloorRequestEvent;
import Messaging.Messages.SystemMessage;
import Messaging.Transceivers.Receivers.Receiver;
import Messaging.Transceivers.Receivers.SerializableReceiver;
import Messaging.Transceivers.Transmitters.Transmitter;
import StatePatternLib.Context;
import Subsystem.ElevatorSubsytem.ElevatorUtilities;
import Subsystem.Subsystem;

import java.time.Duration;
import java.util.*;

import static java.lang.Math.abs;

/**
 * Scheduler class which models a scheduler in the simulation.
 *
 * @version Iteration-3
 *
 * @version Iteration-4
 * Add elevator timers and hard fault handling.
 *
 * @version Iteration-5
 * Add simulation statistics.
 */
public class Scheduler extends Context implements Subsystem {
    private final Transmitter<? extends Receiver> transmitterToFloor;
    private final Transmitter<? extends Receiver> transmitterToElevator;
    private final Receiver receiver;
    private final Map<DestinationEvent, Long> floorRequestsToTime;
    private final ArrayList<ElevatorStateEvent> idleElevators;
    private int totalElevatorsInService;
    private final Map<Integer, Timer> elevatorTimers; // Elevator number mapping to an elevator timer
    final Logger logger;
    final String logId = "SCHEDULER";
    final long ELEVATOR_TIMEOUT_DELAY; // milliseconds
    final double ELEVATOR_TIMEOUT_DELAY_FACTOR = 1.5;

    // Simulation Statistics
    private int totalElevatorMovements; // measured as total elevator move commands sent
    private int totalGophersHandled;
    private boolean simulationEnding; // flag indicating whether this simulation is ending

    // Simulation times are measured in milliseconds,
    // between the current time and midnight, January 1, 1970 UTC.
    private Long simulationStartTime;
    private Long simulationEndTime;

    public Scheduler(Config config,
                     Receiver receiver,
                     Transmitter<? extends Receiver> transmitterToFloor,
                     Transmitter<? extends Receiver> transmitterToElevator) {
        this.receiver = receiver;
        this.transmitterToElevator = transmitterToElevator;
        this.transmitterToFloor = transmitterToFloor;
        floorRequestsToTime = new HashMap<>();
        idleElevators = new ArrayList<>();
        totalElevatorsInService = config.getNumElevators();
        elevatorTimers = new HashMap<>();
        ELEVATOR_TIMEOUT_DELAY = (long) (config.getTravelTime() * ELEVATOR_TIMEOUT_DELAY_FACTOR);
        totalElevatorMovements = 0;
        totalGophersHandled = 0;
        simulationEnding = false;
        // Logging
        logger = new Logger(config.getVerbosity());

        // Initialize first state for this Subsystem's State Machine
        setNextState(new ReceivingState(this));
    }

    /**
     * Remove a DestinationEvent from this Scheduler after service.
     *
     * @param event The DestinationEvent to remove.
     */
    void removeDestinationEvent(DestinationEvent event) {
        floorRequestsToTime.remove(event);
    }

    /**
     * Check if the Scheduler has any pending DestinationEvents.
     * @ return true if there are pending DestinationEvents; false otherwise.
     */
    boolean hasPendingDestinationEvents() {
        return !floorRequestsToTime.isEmpty();
    }

    /**
     * Process event received from the floor.
     *
     * @param event Event to process.
     */
    void storeFloorRequest(FloorRequestEvent event) {
        // Log
        String msg = ("Floor " + event.destinationEvent().destinationFloor() + ": Request made: " + event.destinationEvent().direction() + ".");
        logger.log(Logger.LEVEL.INFO, logId, msg);
        // Store event locally to use in scheduling
        if (!floorRequestsToTime.containsKey(event.destinationEvent()))
            // Only store request if it does not already exist
            floorRequestsToTime.put(event.destinationEvent(), event.time());
            // NB: StoringFloorRequestState will now handle idle Elevator removal
    }

    /**
     * Bind Receiver to this Scheduler's Transmitter to Elevator.
     *
     * @param receiver The Receiver to bind.
     */
    void bindElevatorReceiver(SerializableReceiver receiver) {
        transmitterToElevator.addReceiver(receiver);
    }

    /**
     * Bind Receiver to this Scheduler's Transmitter to Floor.
     *
     * @param receiver The Receiver to bind.
     */
    void bindFloorReceiver(SerializableReceiver receiver) {
        transmitterToFloor.addReceiver(receiver);
    }

    /**
     * Transmit a SystemCommand to a Floor using this Scheduler's Transmitter.
     *
     * @param command The SystemCommand to send to the Floor.
     */
    void transmitToFloor(SystemCommand command) {
        transmitterToFloor.send(command);
    }

    /**
     * Transmit a SystemCommand to an Elevator using this Scheduler's Transmitter.
     *
     * @param command The SystemCommand to send to the Elevator.
     */
    void transmitToElevator(SystemCommand command) {
       transmitterToElevator.send(command);
       if (command instanceof MoveElevatorCommand) {totalElevatorMovements++;}
    }

    /**
     * Query if there are idle elevators.
     *
     * @return true if there are idle Elevators; false otherwise.
     */
    boolean areIdleElevators() {
        return !idleElevators.isEmpty();
    }

    /**
     * Get the closest idle Elevator to an incoming FloorRequestEvent.
     *
     * @param floorRequestEvent The FloorRequestEvent used to determine the
     * closest idle elevator to send.
     *
     * @return ElevatorStateEvent corresponding to closest idle Elevator.
     */
    ElevatorStateEvent getClosestIdleElevator(FloorRequestEvent floorRequestEvent) {

        // Convenience variable to extract and hold target floor
        int targetFloor = floorRequestEvent.destinationEvent().destinationFloor();
        // Distance tracker (in number of floors)
        int distance;
        // Initialize variables for our sort algorithm using the first idle elevator
        int closestIdleElevatorIndex = 0;
        int closestDistance = abs(idleElevators.get(0).currentFloor()- targetFloor);

        // Determine closest idle Elevator in idleElevators
        // Start at 1 to save a loop, since we initialized using 0
        for (int i = 1; i < idleElevators.size(); i++) {
            distance = (abs(idleElevators.get(i).currentFloor() - targetFloor));
            // Case: This Elevator is the new closest Elevator
            if (distance < closestDistance) {
                // Update closest distance to track new minimum
                closestDistance = distance;
                // Catch the idle Elevator index for later reference
                closestIdleElevatorIndex = i;
            }
        }

        // Return the closest idle Elevator
        return idleElevators.remove(closestIdleElevatorIndex);
    }
    /**
     * Register an Elevator as idle by passing its ElevatorStateEvent.
     *
     * @param event The idle ElevatorStateEvent to store.
     */
    void addIdleElevator(ElevatorStateEvent event) {
       idleElevators.add(event);
       // GUI
        logger.updateGui(event.elevatorNum(),event.currentFloor(),3);
    }

    /**
     * Returns true if elevator should stop, false otherwise.
     *
     * @param e Elevator state to check.
     * @return True if elevator should stop, false otherwise.
     */
    boolean isElevatorStopping(ElevatorStateEvent e) {
        // Elevator should stop if tuple (elevator.currentFloor, elevator.Direction)
        // belongs to the union of scheduler.destinationEvents and elevator.destinationEvents.
        //
        // See state diagram of scheduler for additional context.

        // Create new object for union set to avoid funny business of destination events
        // being added to the scheduler's floor requests!
        Set<DestinationEvent> union = new HashSet<>();
        union.addAll(floorRequestsToTime.keySet());
        union.addAll(e.passengerCountMap().keySet());

        if (isMovingOppositeToFutureDirection(e)) {
            return false;
        }
        // Fault: Assume null -> matches against any fault which is important for parity with legacy behavior.
        return union.contains(new DestinationEvent(e.currentFloor(), getElevatorDirection(e), null));
    }

    /**
     * Returns false if the elevator is empty and is moving opposite to the future direction.
     * @return false if elevator is empty and moving opposite to future direction;
     * true otherwise.
     */
    boolean isMovingOppositeToFutureDirection(ElevatorStateEvent event){
        return (event.passengerCountMap().isEmpty() && getElevatorDirection(event) != getOldestFloorRequest().direction());
    }
    DestinationEvent getOldestFloorRequest() {
        if (floorRequestsToTime.isEmpty()) return null;
        Long waitTime = Long.MAX_VALUE;
        DestinationEvent oldestFloor = null;
        for (Map.Entry<DestinationEvent, Long> request : floorRequestsToTime.entrySet()) {
            if (request.getValue() < waitTime) {
                waitTime = request.getValue();
                oldestFloor = request.getKey();
            }
        }
        return oldestFloor;
    }
    /**
     * Returns the direction of the oldest floor request based on the elevator's current floor.
     *
     * @param currentFloor The floor number the elevator is currently on.
     * @return Direction of oldest floor request relative to currentFloor, null if there are no floor requests.
     */
    Direction getDirectionToOldestFloor(int currentFloor) {
        int sourceFloor = getOldestFloorRequest().destinationFloor();
        return (sourceFloor > currentFloor) ? Direction.UP : Direction.DOWN;
    }

    /**
     * Gets the direction an elevator is travelling.
     * Precondition: At least one passenger in the elevator or at least one floor request in floorRequestsToTime.
     *
     * @param event Elevator state to get direction from.
     * @return Direction elevator is travelling.
     */
    Direction getElevatorDirection(ElevatorStateEvent event) {
        // Find direction in elevator if elevator has passengers.
        Direction direction = ElevatorUtilities.getPassengersDirection(event.passengerCountMap().keySet());
        if (direction != null){
            return direction;
        }
        if (floorRequestsToTime.isEmpty()){
            throw new RuntimeException("No passenger on elevator and no floor requests");
        }
        // If on the same floor as the oldest floor request, return direction of the floor request.
        if (event.currentFloor() == getOldestFloorRequest().destinationFloor()){
            return getOldestFloorRequest().direction();
        }
        // Find direction to oldest floor request.
        return getDirectionToOldestFloor(event.currentFloor());
    }

    /**
     * Pop a message from this Subsystem's Receiver buffer.
     */
    SystemMessage receive() {
        return receiver.dequeueMessage();
    }

    /**
     * Starts the timer for an elevator. If the elevator's timer blows its lid,
     * the timer assumes a hard fault has occurred and then handles it.
     * An example of a hard fault would be gophers chewing through the elevator cables.
     *
     * @param elevNum The elevator whose timer should start.
     */
    void startElevatorTimer(int elevNum) {
        class HandleHardFault extends TimerTask {
            @Override
            public void run() {
                String msg = "Hard fault detected (possibly gophers) for elevator " + elevNum + ". Taking elevator out of service.";
                logger.log(Logging.Logger.LEVEL.INFO, logId, msg);
                totalElevatorsInService--; // Take elevator out of service
                elevatorTimers.remove(elevNum);
                totalGophersHandled++;
                // GUI
                logger.updateGui(elevNum, 0, 2);
            }
        }

        Timer timer = new Timer();
        timer.schedule(new HandleHardFault(), ELEVATOR_TIMEOUT_DELAY);
        elevatorTimers.put(elevNum, timer);

        String msg = "Timer started for elevator " + elevNum + ".";
        logger.log(Logging.Logger.LEVEL.DEBUG, logId, msg);
    }

    /**
     * Kills the timer for an elevator.
     *
     * @param elevNum The elevator whose timer should stop.
     */
    void killElevatorTimer(int elevNum) {
        Timer timer = elevatorTimers.get(elevNum);
        if (timer != null) {
            timer.cancel();
            elevatorTimers.remove(elevNum);
            String msg = "Timer killed for elevator " + elevNum + ".";
            logger.log(Logging.Logger.LEVEL.DEBUG, logId, msg);
        }
    }

    /**
     * Sets the flag to start ending simulation.
     */
    void setSimulationEnding() {simulationEnding = true;}

    /**
     * Checks if this simulation has ended.
     *
     * @return True if end of simulation, false otherwise.
     */
    boolean isEndOfSimulation() {
        return simulationEnding
                && (idleElevators.size() == totalElevatorsInService);
    }

    void setSimulationStartTime() {simulationStartTime = System.currentTimeMillis();}

    void setSimulationEndTime() {simulationEndTime = System.currentTimeMillis();}

    /**
     * Returns the total simulation time for a completed simulation.
     *
     * Assumes the simulation has ended and the start and end times
     * for the simulation have been recorded.
     *
     * @return Total simulation time in milliseconds.
     */
    Long getTotalSimulationTime() {
        assert isEndOfSimulation();
        assert simulationStartTime != null;
        assert simulationEndTime != null;

        return simulationEndTime - simulationStartTime;
    }

    /**
     * Displays current simulation statistics to console.
     */
    void displaySimulationStatistics() {
        logger.log(Logger.LEVEL.INFO, logId, "Displaying simulation statistics ...");

        List<String> stats = new LinkedList<>();
        if (isEndOfSimulation()) {
            stats.add("Total simulation time (HH:MM:SS): " + convertMillisToHHMMSS(getTotalSimulationTime()));
        }
        stats.add("Total elevator movements: " + totalElevatorMovements);
        stats.add("Total gophers handled: " + totalGophersHandled);

        for (String stat: stats) {
            logger.log(Logger.LEVEL.INFO, logId, stat);
        }
    }

    private String convertMillisToHHMMSS(long ms) {
        // Reference: https://www.baeldung.com/java-ms-to-hhmmss
        Duration duration = Duration.ofMillis(ms);
        long seconds = duration.getSeconds();
        long HH = seconds / 3600;
        long MM = (seconds % 3600) / 60;
        long SS = seconds % 60;
        HH = duration.toHours();
        MM = duration.toMinutesPart();
        SS = duration.toSecondsPart();
        return String.format("%02d:%02d:%02d", HH, MM, SS);
    }

    /**
     * Start the State Machine, with initial state of ReceivingState.
     * Initial state is set in Constructor.
     */
    @Override
    public void run() {
        while (currentState != null){
            currentState.runState();
        }
    }

}
