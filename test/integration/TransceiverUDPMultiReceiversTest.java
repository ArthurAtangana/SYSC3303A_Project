package integration;

import Messaging.Messages.Direction;
import Messaging.Messages.Events.DestinationEvent;
import Messaging.Messages.Events.ElevatorStateEvent;
import Messaging.Transceivers.Receivers.ReceiverUDP;
import Messaging.Transceivers.Transmitters.TransmitterUDP;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration test that asserts proper send/receive communication between the TransmitterUDP and multiple ReceiverUDPs
 * using a ElevatorStateEvent SystemMessage.
 */
public class TransceiverUDPMultiReceiversTest {

    public static void main(String[] args) {

        System.out.println("Initialize UDP transmitter and receivers.");
        // "Scheduler" Transmitter
        TransmitterUDP schedulerTransmitter = new TransmitterUDP();

        // "Floor" UDP Receivers
        ReceiverUDP floor1Receiver = new ReceiverUDP(1, 5000);
        ReceiverUDP floor2Receiver = new ReceiverUDP(2, 6000);

        // UDP Receiver threads
        Thread floor1Thread = new Thread(floor1Receiver);
        Thread floor2Thread = new Thread(floor2Receiver);

        System.out.println("Starting threads.");
        floor1Thread.start();
        floor2Thread.start();

        System.out.println("Binding receivers to transmitter.");
        schedulerTransmitter.addReceiver(floor1Receiver.getSerializableReceiver());
        schedulerTransmitter.addReceiver(floor2Receiver.getSerializableReceiver());

        // Initializing ElevatorStateEvent for the scheduler to send to the floors
        HashMap<DestinationEvent, Integer> passengerCountMap = new HashMap<>();
        DestinationEvent e = new DestinationEvent(3, Direction.DOWN, null);
        passengerCountMap.put(e, 5);
        ElevatorStateEvent elevatorStateEvent = new ElevatorStateEvent(5, 7, passengerCountMap);

        System.out.println("Scheduler transmitter sending ElevatorStateEvent to floors.");
        schedulerTransmitter.send(elevatorStateEvent);

        System.out.println("Call dequeueMessage on the receivers to read the SystemMessage.");
        ElevatorStateEvent e1 = (ElevatorStateEvent) floor1Receiver.dequeueMessage();
        ElevatorStateEvent e2 = (ElevatorStateEvent) floor2Receiver.dequeueMessage();

        System.out.println("Assert correct information is received.");
        assertEquals(e1.elevatorNum(), 5);
        assertEquals(e1.currentFloor(), 7);
        assertEquals(e1.passengerCountMap().size(), 1);

        assertEquals(e2.elevatorNum(), 5);
        assertEquals(e2.currentFloor(), 7);
        assertEquals(e2.passengerCountMap().size(), 1);

        System.out.println("Test PASSED!\nKill thread and exit test.");
        System.exit(0);
    }
}
