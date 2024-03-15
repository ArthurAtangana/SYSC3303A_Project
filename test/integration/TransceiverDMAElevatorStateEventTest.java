package integration;

import Messaging.Messages.Direction;
import Messaging.Messages.Events.DestinationEvent;
import Messaging.Messages.Events.ElevatorStateEvent;
import Messaging.Transceivers.Receivers.ReceiverDMA;
import Messaging.Transceivers.Receivers.ReceiverUDP;
import Messaging.Transceivers.Transmitters.TransmitterDMA;
import Messaging.Transceivers.Transmitters.TransmitterUDP;

import java.util.HashMap;

public class TransceiverDMAElevatorStateEventTest {

    public static void main(String[] args) {

        System.out.println("Setting up test...");
        // "Scheduler" Transmitter
        TransmitterDMA schedulerTransmitter = new TransmitterDMA();

        // "Floor" UDP Receivers
        ReceiverDMA floor1Receiver = new ReceiverDMA(1);
        // ReceiverUDP floor2Receiver = new ReceiverUDP(2, 6000);
        // ReceiverUDP floor3Receiver = new ReceiverUDP(3, 7000);

        // Binding receivers to transmitter
        schedulerTransmitter.addReceiver(floor1Receiver);
        //schedulerTransmitter.addReceiver(floor2Receiver);
        //schedulerTransmitter.addReceiver(floor3Receiver);

        // Initializing ElevatorStateEvent for the scheduler to send to the floors
        HashMap<DestinationEvent, Integer> passengerCountMap = new HashMap<>();
        DestinationEvent e = new DestinationEvent(3, Direction.DOWN);
        passengerCountMap.put(e, 5);
        ElevatorStateEvent elevatorStateEvent = new ElevatorStateEvent(5, 7, passengerCountMap);

        System.out.println("Scheduler transmitter sending ElevatorStateEvent to floors");
        schedulerTransmitter.send(elevatorStateEvent);

        System.exit(0);
    }
}
