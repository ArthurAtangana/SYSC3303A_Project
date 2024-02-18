package SchedulerSubsystem;

import Messaging.Commands.MoveElevatorCommand;
import Messaging.Direction;
import Messaging.Events.DestinationEvent;
import Messaging.Events.ElevatorStateEvent;
import Messaging.Events.FloorRequestEvent;
import Messaging.Receivers.DMA_Receiver;
import Messaging.SystemMessage;
import Messaging.Transmitters.DMA_Transmitter;
import Networking.Messages.ElevatorStateEvent;
import com.sun.jdi.InvalidTypeException;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(Enclosed.class)
public class Scheduler implements Runnable {
    private final DMA_Transmitter transmitterToFloor;
    private final DMA_Transmitter transmitterToElevator;
    private final DMA_Receiver receiver;
    private final Map<DestinationEvent, Long> floorRequestsToTime;
    private ArrayList<ElevatorStateEvent> idleElevators;

    public Scheduler(DMA_Receiver receiver, DMA_Transmitter transmitterToFloor, DMA_Transmitter transmitterToElevator) {
        this.receiver = receiver;
        this.transmitterToElevator = transmitterToElevator;
        this.transmitterToFloor = transmitterToFloor;
        floorRequestsToTime = new HashMap<>();
        idleElevators = new ArrayList<ElevatorStateEvent>();
    }

    /**
     * Process event received from the floor.
     *
     * @param event Event to process.
     */
    private void storeFloorRequest(FloorRequestEvent event) {
        // Store event locally to use in scheduling
        if (!floorRequestsToTime.containsKey(event.destinationEvent()))
            // Only store request if it does not already exist
            floorRequestsToTime.put(event.destinationEvent(), event.time());
        if (!idleElevators.isEmpty()){
            processElevatorEvent(idleElevators.remove(0));
        }
    }
    private Direction getOldestFloorDir() {
        return Direction.UP;
    }

    /**
     * Returns true if elevator should stop, false otherwise.
     *
     * @param e Elevator state to check.
     * @return True if elevator should stop, false otherwise.
     */
    private boolean isElevatorStopping(ElevatorStateEvent e) {
        // Elevator should stop if tuple (elevator.currentFloor, elevator.Direction)
        // belongs to the union of scheduler.destinationEvents and elevator.destinationEvents.
        //
        // See state diagram of scheduler for additional context.

        // Create new object for union set to avoid funny business of destination events
        // being added to the scheduler's floor requests!
        Set<DestinationEvent> union = new HashSet<>();
        union.addAll(floorRequestsToTime.keySet());
        union.addAll(e.passengerCountMap().keySet());

        return union.contains(new DestinationEvent(e.currentFloor(), getElevatorDirection(e)));
    }

    /**
     * Gets the direction an elevator is travelling.
     * Precondition: At least one passenger in the elevator or at least one floor request in floorRequestsToTime.
     *
     * @param event Elevator state to get direction from.
     * @return Direction elevator is travelling.
     */
    private Direction getElevatorDirection(ElevatorStateEvent event) {
        // Find direction in elevator if elevator has passengers.
        Direction direction = null;
        for (DestinationEvent e : event.passengerCountMap().keySet()) {
            if (direction == null) {
                direction = e.direction();
            }
            if (direction != e.direction()) {
                throw new RuntimeException("Mismatched passenger direction in elevator");
            }
        }
        // Find direction from oldest floor request.
        if (direction == null) {
            direction = getOldestFloorDirFromElevator(event.currentFloor());
        }
        // No passengers on elevator and no floor requests. (Goes against precondition)
        if (direction == null) {
            throw new RuntimeException("No passenger on elevator and no floor requests");
        }
        return direction;
    }

    /**
     * Returns the direction of the oldest floor request based on the elevator's current floor.
     *
     * @param currentFloor The floor number the elevator is currently on.
     * @return Direction of oldest floor request relative to currentFloor, null if there are no floor requests.
     */
    private Direction getOldestFloorDirFromElevator(int currentFloor) {
        if (floorRequestsToTime.isEmpty()) return null;
        Long waitTime = Long.MAX_VALUE;
        int sourceFloor = -1;
        for (Map.Entry<DestinationEvent, Long> request : floorRequestsToTime.entrySet()) {
            if (request.getValue() < waitTime) {
                waitTime = request.getValue();
                sourceFloor = request.getKey().destinationFloor();
            }
        }
        return (sourceFloor > currentFloor) ? Direction.UP : Direction.DOWN;
    }

    /**
     *  process elevator event with elevator state (current floor, direction, passengerList)
     *  and sends it to the floor.
     * @param event an elevator state event
     */
    private void processElevatorEvent(ElevatorStateEvent event) {
        if (isElevatorStopping(event)) // Stop elevator for a load
            new Thread(new Loader(event, transmitterToFloor, transmitterToElevator)).start();
        // TODO: redo condition coz floorRequests doesnt exists anymore.
        else if (floorRequests.isEmpty()) {
            idleElevators.add(event);
        else // Keep moving
            transmitterToElevator.send(new MoveElevatorCommand(event.elevatorNum(), getElevatorDirection(event)));
    }

    /**
     * Process the given event, identify type and select an action or activity based on it.
     *
     * @param event The event to process
     * @throws InvalidTypeException If it receives an event type this class cannot handle
     */
    private void processMessage(SystemMessage event) throws InvalidTypeException {
        // Note: Cannot switch on type, if we want to refactor selection, look into visitor pattern.
        if (event instanceof ElevatorStateEvent)
            processElevatorEvent((ElevatorStateEvent) event);
        else if (event instanceof FloorRequestEvent)
            storeFloorRequest((FloorRequestEvent) event);
        else // Default, should never happen
            throw new InvalidTypeException("Event type received cannot be handled by this subsystem.");
    }

    @Override
    public void run() {
        while (true){
            try {
                processMessage(receiver.receive());
            } catch (InvalidTypeException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /* Test */

    /**
     * Nested test class for class Scheduler. Contains unit tests for critical private methods.
     *
     * See README section Test for additional context.
     */
    public static class SchedulerTest {

        DMA_Receiver receiver, mockFloorReceiver, mockElevatorReceiver;
        DMA_Transmitter mockTransmitterToFloor, mockTransmitterToElevator;
        Scheduler scheduler;
        @BeforeEach
        public void setUp() {
            receiver = new DMA_Receiver();
            mockFloorReceiver = new DMA_Receiver();
            mockElevatorReceiver = new DMA_Receiver();
            mockTransmitterToFloor = new DMA_Transmitter(mockFloorReceiver);
            mockTransmitterToElevator = new DMA_Transmitter((mockElevatorReceiver));
            scheduler = new Scheduler(receiver, mockTransmitterToFloor, mockTransmitterToElevator);
        }

        /**
         * Positive test for isElevatorStopping.
         */
        @Test
        @DisplayName("isElevatorStopping positive test")
        public void testPositiveIsElevatorStopping() {
            // Arrange
            int floor = 1; // Same floor for elevator state event and destination event
            DestinationEvent destinationEvent = new DestinationEvent(floor, Direction.UP);
            HashSet<DestinationEvent> schedulerDestinationEvents = new HashSet<>();
            schedulerDestinationEvents.add(destinationEvent);
            // FIXME
            scheduler.floorRequests = schedulerDestinationEvents;

            HashMap<DestinationEvent, Integer> elevatorDestinationEvents = new HashMap<>();
            elevatorDestinationEvents.put(destinationEvent, 1);
            ElevatorStateEvent elevatorStateEvent = new ElevatorStateEvent(1, floor, elevatorDestinationEvents);

            // Act
            boolean result = scheduler.isElevatorStopping(elevatorStateEvent);

            // Assert
            assertTrue(result);
        }
    }
}
