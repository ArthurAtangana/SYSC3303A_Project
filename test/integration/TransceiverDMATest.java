package integration;

import Messaging.Messages.Direction;
import Messaging.Messages.Events.DestinationEvent;
import Messaging.Messages.Events.ElevatorStateEvent;
import Messaging.Transceivers.Receivers.ReceiverDMA;
import Messaging.Transceivers.Transmitters.TransmitterDMA;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Integration test that asserts proper send/receive communication between the TransmitterDMA and the ReceiverDMA
 * using an ElevatorStateEvent SystemMessage.
 */
public class TransceiverDMATest {

    public static void main(String[] args) {
        System.out.println("Initialize DMA Transmitter and Receiver");
        TransmitterDMA schedulerTransmitter = new TransmitterDMA();
        ReceiverDMA floor1Receiver = new ReceiverDMA(1);

        System.out.println("Binding receiver to transmitter");
        schedulerTransmitter.addReceiver(floor1Receiver);

        System.out.println("Create an ElevatorStateEvent SystemMessage");
        HashMap<DestinationEvent, Integer> passengerCountMap = new HashMap<>();
        DestinationEvent e = new DestinationEvent(3, Direction.DOWN);
        passengerCountMap.put(e, 5);
        ElevatorStateEvent elevatorStateEvent = new ElevatorStateEvent(5, 7, passengerCountMap);

        System.out.println("Scheduler transmitter sending ElevatorStateEvent to floors");
        schedulerTransmitter.send(elevatorStateEvent);

        System.out.println("Call dequeueMessage on the ReceiverDMA to read the SystemMessage.");
        ElevatorStateEvent receivedDestEvent = (ElevatorStateEvent) floor1Receiver.dequeueMessage();

        System.out.println("Assert correct information is received.");
        assertEquals(5, receivedDestEvent.elevatorNum());
        assertEquals(7, receivedDestEvent.currentFloor());
        assertEquals(5, receivedDestEvent.passengerCountMap().get(e));

        System.out.println("Test PASSED!\nKill thread and exit test.");
        System.exit(0);
    }
}