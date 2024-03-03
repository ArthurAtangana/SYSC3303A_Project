package unit.Networking;

import Messaging.Messages.Direction;
import Messaging.Messages.Events.DestinationEvent;
import Messaging.Messages.Events.SystemEvent;
import Messaging.Transceivers.Receivers.ReceiverDMA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReceiverDMATest {

    ReceiverDMA receiver;
    SystemEvent systemEvent;
    @BeforeEach
    void setUp() {
        this.receiver = new ReceiverDMA(-1);
    }
    @Test
    void setAndReceiveMessage() {
        systemEvent = new DestinationEvent(2, Direction.UP);
        receiver.setMessage(systemEvent);
        DestinationEvent event = (DestinationEvent) receiver.receive();
        assertEquals(2,event.destinationFloor());
        assertEquals(Direction.UP, event.direction());
    }
}