package integration;

import Messaging.Messages.Direction;
import Messaging.Messages.Events.DestinationEvent;
import Messaging.Messages.Events.ElevatorStateEvent;
import Messaging.Messages.Events.PassengerLoadEvent;
import Messaging.Transceivers.Receivers.ReceiverUDP;
import Messaging.Transceivers.Transmitters.TransmitterUDP;

import java.util.ArrayList;
import java.util.HashMap;

public class TransceiverUDPElevatorStateEventTest {


    public static void main(String[] args) {

        System.out.println("Setting up test...");
        // "Scheduler" Transmitter
        TransmitterUDP schedulerTransmitter = new TransmitterUDP();

        // "Floor" UDP Receivers
        ReceiverUDP floor1Receiver = new ReceiverUDP(1, 5000);
        // ReceiverUDP floor2Receiver = new ReceiverUDP(2, 6000);
        // ReceiverUDP floor3Receiver = new ReceiverUDP(3, 7000);

        // UDP Receiver threads
        Thread floor1Thread = new Thread(floor1Receiver);
        // Thread floor2Thread = new Thread(floor2Receiver);
        // Thread floor3Thread = new Thread(floor3Receiver);

        // Starting threads
        floor1Thread.start();
        //floor2Thread.start();
        //floor3Thread.start();

        // Binding receivers to transmitter
        schedulerTransmitter.addReceiver(floor1Receiver);
        //schedulerTransmitter.addReceiver(floor2Receiver);
        //schedulerTransmitter.addReceiver(floor3Receiver);

        // Initializing ElevatorStateEvent for the scheduler to send to the floors
        HashMap<DestinationEvent, Integer> passengerCountMap = new HashMap<>();
        DestinationEvent e = new DestinationEvent(3, Direction.DOWN);
        passengerCountMap.put(e, 5);
        ElevatorStateEvent elevatorStateEvent = new ElevatorStateEvent(5, 7, passengerCountMap);

        ArrayList<DestinationEvent> passengers = new ArrayList<>();
        passengers.add(e);
        PassengerLoadEvent p = new PassengerLoadEvent(2, passengers);

        System.out.println("Scheduler transmitter sending ElevatorStateEvent to floors");
        // schedulerTransmitter.send(elevatorStateEvent);
        schedulerTransmitter.send(p);

        System.exit(0);
    }
}
