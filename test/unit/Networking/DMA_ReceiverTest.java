package unit.Networking;

import Networking.Direction;
import Networking.Events.DestinationEvent;
import Networking.Events.ElevatorSystemEvent;
import Networking.Receivers.DMA_Receiver;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DMA_ReceiverTest {

    DMA_Receiver receiver;
    ElevatorSystemEvent systemEvent;
    @Test
    void setUp() {
        this.receiver = new DMA_Receiver();
    }
    @Test
    void setAndReceiveMessage() {
        setUp();
        systemEvent = new DestinationEvent(2, Direction.UP);
        receiver.setMessage(systemEvent);
        DestinationEvent event = (DestinationEvent) receiver.receive();
        assertEquals(2,event.destinationFloor());
        assertEquals(Direction.UP, event.direction());
    }
}