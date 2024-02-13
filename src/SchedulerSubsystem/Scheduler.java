package SchedulerSubsystem;

import Networking.Direction;
import Networking.Messages.DestinationEvent;
import Networking.Messages.ElevatorStateEvent;
import Networking.Messages.MoveElevatorCommand;
import Networking.Messages.SystemMessage;
import Networking.Receivers.DMA_Receiver;
import Networking.Transmitters.DMA_Transmitter;
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
        // Elevator should stop if (elevator.currentFloor, elevator.Direction)
        // belongs to the union of scheduler.destinationEvents and elevator.destinationEvents.
        //
        // See state diagram of scheduler for additional context.

        Set<DestinationEvent> union = unionOfDestinationEvents(floorRequests, e.passengerCountMap().keySet());
        Set<Integer> destinationFloors = filterDestinationFloors(union);
        return destinationFloors.contains(e.currentFloor());
    }

    /**
     *  process elevator event with elevator state (current floor, direction, passengerList)
     *  and sends it to the floor.
     * @param event an elevator state event
     */
    private void processElevatorEvent(ElevatorStateEvent event) {
        if (isElevatorStopping(event)) // Stop elevator for a load
            new Thread(new Loader(event, transmitterToFloor, transmitterToElevator)).start();
        else // Keep moving
            // Note(@braeden): event.direction() is deprecated
            transmitterToElevator.send(new MoveElevatorCommand(event.elevatorNum(), event.direction()));
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

    /**
     * Filters destination floors from destination events.
     *
     * @param events Set of destination events.
     * @return Set of destination floors.
     */
    private Set<Integer> filterDestinationFloors(Set<DestinationEvent> events) {
        //TODO(@braeden): delete method
        Set<Integer> destinationFloors = new HashSet<>();
        for (DestinationEvent e: events) {
            destinationFloors.add(e.destinationFloor());
        }
        return destinationFloors;
    }

    /**
     * Takes union of two destination event sets.
     *
     * @param A Set of destination events.
     * @param B Set of destination events.
     * @return Union of both sets.
     */
    private Set<DestinationEvent> unionOfDestinationEvents(
            // TODO(@braeden): delete method, use set.addAll() instead
            Collection<DestinationEvent> A,
            Collection<DestinationEvent> B) {
        HashSet<DestinationEvent> union = new HashSet<DestinationEvent>();
        for (DestinationEvent a: A) {
            if (B.contains(a)) {
                union.add(a);
            }
        }
        return union;
    }

    /**
     * Sets destination events of this scheduler.
     *
     * @param events Set of destination events to be set.
     */
    public void setFloorRequests(Set<DestinationEvent> events) {
        // Documenting a setter for a set is unsettling ...
        this.floorRequests = events;
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
     * Nested test class for Scheduler unit tests.
     *
     * See README for reasoning.
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
            scheduler.setFloorRequests(schedulerDestinationEvents);

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
