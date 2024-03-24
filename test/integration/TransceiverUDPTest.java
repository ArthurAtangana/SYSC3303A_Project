package integration;

import Messaging.Messages.Direction;
import Messaging.Messages.Events.DestinationEvent;
import Messaging.Transceivers.Receivers.ReceiverUDP;
import Messaging.Transceivers.Transmitters.TransmitterUDP;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration test that asserts proper send/receive communication between the TransmitterUDP and the ReceiverUDP
 * using a DestinationEvent SystemMessage.
 */
public class TransceiverUDPTest {

    public static void main(String[] args) {
        System.out.println("Initialize ReceiverUDP thread to receive from port 5000.");
        ReceiverUDP udp_receiver = new ReceiverUDP(0, 5000);
        Thread receiver = new Thread(udp_receiver);
        receiver.start();

        System.out.println("Initialize TransmitterUDP to send to port 5000.");
        TransmitterUDP udp_transmitter = new TransmitterUDP();
        udp_transmitter.addReceiver(udp_receiver.getSerializableReceiver());

        System.out.println("Create a DestinationEvent SystemMessage.");
        DestinationEvent destEvent = new DestinationEvent(2, Direction.UP, null);
        udp_transmitter.send(destEvent);

        System.out.println("Call dequeueMessage on the ReceiverUDP to read the SystemMessage.");
        DestinationEvent receivedDestEvent = (DestinationEvent) udp_receiver.dequeueMessage();

        System.out.println("Assert correct information is received.");
        assertEquals(receivedDestEvent.destinationFloor(), 2);
        assertEquals(receivedDestEvent.direction(), Direction.UP);

        System.out.println("Test PASSED!\nKill thread and exit test.");
        System.exit(0);
    }

}
