package integration;

import Messaging.Messages.Direction;
import Messaging.Messages.Events.DestinationEvent;
import Messaging.Transceivers.Receivers.UDP_Receiver;
import Messaging.Transceivers.Transmitters.UDP_Transmitter;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration test that asserts proper send/receive communication between the UDP_Transmitter and the UDP_Receiver.
 */
public class UDP_TransceiverTest {

    public static void main(String[] args) {
        System.out.println("Initialize UDP_Receiver thread to receive from port 5000.");
        UDP_Receiver udp_receiver = new UDP_Receiver(0, 5000);
        Thread receiver = new Thread(udp_receiver);
        receiver.start();

        System.out.println("Initialize UDP_Transmitter to send to port 5000.");
        UDP_Transmitter udp_transmitter = new UDP_Transmitter(5000);

        System.out.println("Create a DestinationEvent SystemMessage.");
        DestinationEvent destEvent = new DestinationEvent(2, Direction.UP);
        udp_transmitter.send(destEvent);

        System.out.println("Call receive on the UDP_Receiver to read the SystemMessage.");
        DestinationEvent receivedDestEvent = (DestinationEvent) udp_receiver.dequeueMessage();

        System.out.println("Assert correct information is received.");
        assertEquals(receivedDestEvent.destinationFloor(), 2);
        assertEquals(receivedDestEvent.direction(), Direction.UP);

        System.out.println("Test PASSED!\nKill thread and exit test.");
        System.exit(0);
    }

}
