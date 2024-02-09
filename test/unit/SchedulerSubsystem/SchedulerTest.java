package unit.SchedulerSubsystem;

import Networking.Direction;
import Networking.Messages.DestinationEvent;
import Networking.Messages.ElevatorStateEvent;
import Networking.Receivers.DMA_Receiver;
import Networking.Receivers.Receiver;
import Networking.Transmitters.DMA_Transmitter;
import Networking.Transmitters.Transmitter;
import SchedulerSubsystem.Scheduler;
import java.util.ArrayList;
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
    @Test
    public void isElevatorStoppingTest() {
        // Arrange
        ArrayList<DestinationEvent> destinationEvents = new ArrayList<>();
        ElevatorStateEvent e = new ElevatorStateEvent(1, 1, Direction.UP, destinationEvents);

        // Act
        boolean result = s.isElevatorStopping(e);

        // Assert
        assertEquals(false, result);
    }
}
