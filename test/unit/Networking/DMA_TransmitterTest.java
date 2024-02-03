package unit.Networking;

import Networking.Direction;
import Networking.Events.DestinationEvent;
import Networking.Events.ElevatorSystemEvent;
import Networking.Receivers.DMA_Receiver;
import Networking.Transmitters.DMA_Transmitter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DMA_TransmitterTest {

    private DMA_Receiver receiver;
    private DMA_Transmitter transmitter;
    ElevatorSystemEvent systemEvent;

    @Test
    void setUp(){
        this.receiver = new DMA_Receiver();
        this.transmitter = new DMA_Transmitter(receiver);
    }

    @Test
    void send() {
        setUp();
        systemEvent = new DestinationEvent(2, Direction.UP);
        transmitter.send(systemEvent);
        DestinationEvent event = (DestinationEvent) receiver.receive();
        assertEquals(2, event.destinationFloor());
        assertEquals(Direction.UP, event.direction());
    }
}