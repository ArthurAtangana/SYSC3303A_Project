package unit.Networking;

import Messaging.Messages.Direction;
import Messaging.Messages.Events.DestinationEvent;
import Messaging.Messages.Events.SystemEvent;
import Messaging.Transceivers.Receivers.ReceiverDMA;
import Messaging.Transceivers.Transmitters.TransmitterDMA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransmitterDMATest {

    private ReceiverDMA receiver;
    private TransmitterDMA transmitter;
    SystemEvent systemEvent;

    @BeforeEach
    void setUp(){
        this.receiver = new ReceiverDMA(-1);
        this.transmitter = new TransmitterDMA();
        this.transmitter.addReceiver(receiver);
    }

    @Test
    void send() {
        systemEvent = new DestinationEvent(2, Direction.UP, null);
        transmitter.send(systemEvent);
        DestinationEvent event = (DestinationEvent) receiver.dequeueMessage();
        assertEquals(2, event.destinationFloor());
        assertEquals(Direction.UP, event.direction());
    }
}