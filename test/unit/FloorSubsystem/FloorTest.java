package unit.FloorSubsystem;

import FloorSubsystem.Floor;
import Networking.Direction;
import Networking.Events.ElevatorSystemEvent;
import Networking.Events.FloorInputEvent;
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
    ArrayList<FloorInputEvent> floorInputEvents;
    FloorInputEvent p1;

    @BeforeEach
    void setUp() {
        receiver = new DMA_Receiver();
        f = new Floor(1, receiver);
        floorInputEvents = new ArrayList<>();
        p1 = new FloorInputEvent(5, 1, Direction.UP, 3);
        floorInputEvents.add(p1);
    }

    @Test
    void sendPassengerRequestToSchedulerTest() {
        // Arrange

        // Act

        // Assert
    }

    @Test
    void removeArrivedPassengersTest() {
        // Arrange

        // Act

        // Assert
    }




}