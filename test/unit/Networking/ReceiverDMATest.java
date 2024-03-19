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

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReceiverDMATest {

    ReceiverDMA receiver;
    @BeforeEach
    void setUp() {
        this.receiver = new ReceiverDMA(0);
    }
    @Test
    void setReceiveEvent() {
        // Verify single set/receive scenario works
        SystemEvent sndEvent = new DestinationEvent(2, Direction.UP, null);
        receiver.receiveDMA(sndEvent);

        SystemMessage receivedEvent = receiver.dequeueMessage();
        assertEquals(receivedEvent, sndEvent);
    }

    @Test
    void setReceiveEventFIFO() {
        // Verify receiver Queue behavior (FIFO)
        SystemEvent sndEvent = new DestinationEvent(1, Direction.UP, null);
        receiver.receiveDMA(sndEvent);
        SystemEvent sndEvent2 = new DestinationEvent(2, Direction.UP, null);
        receiver.receiveDMA(sndEvent2);
        SystemEvent sndEvent3 = new DestinationEvent(3, Direction.UP, null);
        receiver.receiveDMA(sndEvent3);

        assertEquals(receiver.dequeueMessage(), sndEvent);
        assertEquals(receiver.dequeueMessage(), sndEvent2);
        assertEquals(receiver.dequeueMessage(), sndEvent3);
    }

    @Test
    void setReceiveCommand() {
        // Verify commands can be properly filtered by key

        // Key == 1, does not match with receiver key (0), should be ignored
        SystemCommand sndCommand1 = new MoveElevatorCommand(1, null);
        receiver.receiveDMA(sndCommand1);
        // Key == 0, matches, should be received
        SystemCommand sndCommand2 = new MoveElevatorCommand(0, null);
        receiver.receiveDMA(sndCommand2);

        // Check that first message is ignored, and second received (since receiver has queue behavior)
        assertEquals(receiver.dequeueMessage(), sndCommand2);
    }
}