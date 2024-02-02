package unit.FloorSubsystem;

import FloorSubsystem.Floor;
import Networking.Direction;
import Networking.Events.ElevatorSystemEvent;
import Networking.Events.FloorButtonPressedEvent;
import Networking.Events.Passenger;
import Networking.Receivers.DMA_Receiver;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for class Floor.
 */
public class FloorTest {

    DMA_Receiver receiver;
    Floor f;
    ArrayList<Passenger> passengers;
    Passenger p1;

    @BeforeEach
    void setUp() {
        receiver = new DMA_Receiver();
        f = new Floor(1, receiver);
        passengers = new ArrayList<>();
        p1 = new Passenger(5, 1, Direction.UP, 3);
        passengers.add(p1);
    }

    @Test
    void sendPassengerRequestToSchedulerTest() {
        // Arrange
        ElevatorSystemEvent event = new FloorButtonPressedEvent(p1);

        // Act
        f.sendPassengerRequestToScheduler(event);

        // Assert
        assertEquals(event, receiver.receive());
    }

    @Test
    void removeArrivedPassengersTest() {
        // Arrange
        f.setPassengers(passengers);

        // Act
        f.sendFloorButtonPressedEvent();

        // Assert
        assertTrue(f.getPassengers().isEmpty());
    }




}