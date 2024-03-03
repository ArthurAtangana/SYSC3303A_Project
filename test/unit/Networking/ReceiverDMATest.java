package unit.Networking;

import Messaging.Messages.Commands.MoveElevatorCommand;
import Messaging.Messages.Commands.SystemCommand;
import Messaging.Messages.Direction;
import Messaging.Messages.Events.DestinationEvent;
import Messaging.Messages.Events.SystemEvent;
import Messaging.Messages.SystemMessage;
import Messaging.Transceivers.Receivers.ReceiverDMA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReceiverDMATest {

    ReceiverDMA receiver;
    @BeforeEach
    void setUp() {
        this.receiver = new ReceiverDMA(0);
    }
    @Test
    void setReceiveEvent() {
        // Verify single set/receive scenario works
        SystemEvent sndEvent = new DestinationEvent(2, Direction.UP);
        receiver.setMessage(sndEvent);

        SystemMessage receivedEvent = receiver.receive();
        assertEquals(receivedEvent, sndEvent);
    }

    @Test
    void setReceiveEventFIFO() {
        // Verify receiver Queue behavior (FIFO)
        SystemEvent sndEvent = new DestinationEvent(1, Direction.UP);
        receiver.setMessage(sndEvent);
        SystemEvent sndEvent2 = new DestinationEvent(2, Direction.UP);
        receiver.setMessage(sndEvent2);
        SystemEvent sndEvent3 = new DestinationEvent(3, Direction.UP);
        receiver.setMessage(sndEvent3);

        assertEquals(receiver.receive(), sndEvent);
        assertEquals(receiver.receive(), sndEvent2);
        assertEquals(receiver.receive(), sndEvent3);
    }

    @Test
    void setReceiveCommand() {
        // Verify commands can be properly filtered by key

        // Key == 1, does not match with receiver key (0), should be ignored
        SystemCommand sndCommand1 = new MoveElevatorCommand(1, null);
        receiver.setMessage(sndCommand1);
        // Key == 0, matches, should be received
        SystemCommand sndCommand2 = new MoveElevatorCommand(0, null);
        receiver.setMessage(sndCommand2);

        // Check that first message is ignored, and second received (since receiver has queue behavior)
        assertEquals(receiver.receive(), sndCommand2);
    }
}