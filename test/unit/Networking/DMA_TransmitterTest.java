package unit.Networking;

import Messaging.Direction;
import Messaging.Events.DestinationEvent;
import Messaging.Events.SystemEvent;
import Messaging.Receivers.DMA_Receiver;
import Messaging.Transmitters.DMA_Transmitter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DMA_TransmitterTest {

    private DMA_Receiver receiver;
    private DMA_Transmitter transmitter;
    SystemEvent systemEvent;

    @BeforeEach
    void setUp(){
        this.receiver = new DMA_Receiver();
        this.transmitter = new DMA_Transmitter(receiver);
    }

    @Test
    void send() {
        systemEvent = new DestinationEvent(2, Direction.UP);
        transmitter.send(systemEvent);
        DestinationEvent event = (DestinationEvent) receiver.receive();
        assertEquals(2, event.destinationFloor());
        assertEquals(Direction.UP, event.direction());
    }
}