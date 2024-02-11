package unit.SchedulerSubsystem;

import Networking.Direction;
import Networking.Messages.DestinationEvent;
import Networking.Messages.ElevatorStateEvent;
import Networking.Receivers.DMA_Receiver;
import Networking.Transmitters.DMA_Transmitter;
import SchedulerSubsystem.Scheduler;

import java.util.HashMap;
import java.util.HashSet;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for class Scheduler.
 */
public class SchedulerTest {

    DMA_Receiver receiver, mockFloorReceiver, mockElevatorReceiver;
    DMA_Transmitter mockTransmitterToFloor, mockTransmitterToElevator;
    Scheduler s;
    @BeforeEach
    public void setUp() {
        receiver = new DMA_Receiver();
        mockFloorReceiver = new DMA_Receiver();
        mockElevatorReceiver = new DMA_Receiver();
        mockTransmitterToFloor = new DMA_Transmitter(mockFloorReceiver);
        mockTransmitterToElevator = new DMA_Transmitter((mockElevatorReceiver));
        s = new Scheduler(receiver, mockTransmitterToFloor, mockTransmitterToElevator);
    }

    /**
     * Positive test for isElevatorStopping.
     */
    @Test
    @DisplayName("isElevatorStopping positive test")
    public void testPositiveIsElevatorStopping() {
        // Elevator should stop when elevator.currentFloor belong to the union of
        // scheduler.destinationEvents and elevator.destinationEvents.
        //
        // See state diagram of scheduler for additional context.

        // Arrange
        int floor = 1; // Same floor for elevator state event and destination event
        DestinationEvent destinationEvent = new DestinationEvent(floor, Direction.UP);
        HashSet<DestinationEvent> schedulerDestinationEvents = new HashSet<>();
        schedulerDestinationEvents.add(destinationEvent);
        s.setFloorRequests(schedulerDestinationEvents);

        HashMap<DestinationEvent, Integer> elevatorDestinationEvents = new HashMap<DestinationEvent, Integer>();
        elevatorDestinationEvents.put(destinationEvent, 1);
        ElevatorStateEvent elevatorStateEvent = new ElevatorStateEvent(1, floor, Direction.UP, elevatorDestinationEvents);

        // Act
        boolean result = s.isElevatorStopping(elevatorStateEvent);

        // Assert
        assertEquals(true, result);
    }
}
