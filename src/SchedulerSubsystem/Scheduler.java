package SchedulerSubsystem;

import Messaging.Direction;
import Messaging.Events.DestinationEvent;
import Messaging.Events.ElevatorStateEvent;
import Messaging.Commands.MoveElevatorCommand;
import Messaging.SystemMessage;
import Messaging.Receivers.DMA_Receiver;
import Messaging.Transmitters.DMA_Transmitter;
import com.sun.jdi.InvalidTypeException;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class Scheduler implements Runnable {
    private final DMA_Transmitter transmitterToFloor;
    private final DMA_Transmitter transmitterToElevator;
    private final DMA_Receiver receiver;
    private Set<DestinationEvent> floorRequests;

    public Scheduler(DMA_Receiver receiver, DMA_Transmitter transmitterToFloor, DMA_Transmitter transmitterToElevator) {
        this.receiver = receiver;
        this.transmitterToElevator = transmitterToElevator;
        this.transmitterToFloor = transmitterToFloor;
        floorRequests = new HashSet<DestinationEvent>();
    }

    /**
     * Process event received from the floor.
     *
     * @param destinationEvent Destination event received from floor.
     */
    private void processDestinationEvent(DestinationEvent destinationEvent) {
        // Store event locally to use in scheduling
        floorRequests.add(destinationEvent);
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
        Set<DestinationEvent> union = new HashSet<DestinationEvent>();
        union.addAll(floorRequests);
        union.addAll(e.passengerCountMap().keySet());

        return union.contains(new DestinationEvent(e.currentFloor(), getElevatorDirection(e)));
    }

    /**
     * Gets the direction an elevator is travelling.
     *
     * @param e Elevator state to get direction from.
     * @return Direction elevator is travelling.
     */
    private Direction getElevatorDirection(ElevatorStateEvent e) {
        // TODO(@braeden): should always return a valid direction or throw error
        //   I'm worried about returning a null value!
        // TODO(@braeden): should also assert that every direction is same?
        //   see Elevator.getDirection
        Direction direction = null;
        for (DestinationEvent event: e.passengerCountMap().keySet()) {
            direction = event.direction();
        }
        return direction;
    };

    /**
     *  process elevator event with elevator state (current floor, direction, passengerList)
     *  and sends it to the floor.
     * @param event an elevator state event
     */
    private void processElevatorEvent(ElevatorStateEvent event) {
        if (isElevatorStopping(event)) // Stop elevator for a load
            new Thread(new Loader(event, transmitterToFloor, transmitterToElevator)).start();
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
        else if (event instanceof DestinationEvent)
            processDestinationEvent((DestinationEvent) event);
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
            scheduler.floorRequests = schedulerDestinationEvents;

            HashMap<DestinationEvent, Integer> elevatorDestinationEvents = new HashMap<DestinationEvent, Integer>();
            elevatorDestinationEvents.put(destinationEvent, 1);
            ElevatorStateEvent elevatorStateEvent = new ElevatorStateEvent(1, floor, Direction.UP, elevatorDestinationEvents);

            // Act
            boolean result = scheduler.isElevatorStopping(elevatorStateEvent);

            // Assert
            assertEquals(true, result);
        }
    }
}
