package unit.FloorSubsystem;

import FloorSubsystem.Floor;
import Networking.Direction;
import Networking.Events.ElevatorSystemEvent;
import Networking.Events.FloorButtonPressedEvent;
import Networking.Events.Passenger;
import Networking.Receivers.DMA_Receiver;
import SchedulerSubsystem.Scheduler;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for class Floor.
 */
public class FloorTest {

    @Test
    void sendPassengerRequestToSchedulerTest() {
        // Arrange
        DMA_Receiver receiver = new DMA_Receiver();
        Floor f = new Floor(1, receiver);
        Passenger passenger = new Passenger(5, 1, Direction.UP, 3);
        ElevatorSystemEvent event = new FloorButtonPressedEvent(passenger);

        // Act
        f.sendPassengerRequestToScheduler(event);

        // Assert
        assertEquals(event, receiver.receive());
    }




}