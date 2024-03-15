package unit.Networking;

import Messaging.Messages.Commands.MovePassengersCommand;
import Messaging.Messages.Direction;
import Messaging.Messages.Events.DestinationEvent;
import Messaging.Messages.Events.ElevatorStateEvent;
import Messaging.Messages.SystemMessage;
import Messaging.Transceivers.TransceiverUtility;
import Messaging.Messages.SystemMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransceiverSerializationTest {
    DestinationEvent destEvent;
    HashMap<DestinationEvent, Integer> passengerCountMap;

    @BeforeEach
    void setUp() {
        destEvent = new DestinationEvent(3, Direction.DOWN);
        passengerCountMap = new HashMap<>();
        passengerCountMap.put(destEvent, 2);
    }

    @Test
    void DestinationEventTest() {
        byte[] serialE = TransceiverUtility.serializeSystemMessage(destEvent);

        SystemMessage s = TransceiverUtility.deserializeSystemMessage(serialE);
        DestinationEvent deserializedE = (DestinationEvent) s;

        assertEquals(deserializedE.direction(), Direction.DOWN);
        assertEquals(deserializedE.destinationFloor(), 3);
    }

    @Test
    void ElevatorStateEventTest() {
        ElevatorStateEvent e = new ElevatorStateEvent(5, 7, passengerCountMap);

        byte[] serialE = TransceiverUtility.serializeSystemMessage(e);
        SystemMessage s = TransceiverUtility.deserializeSystemMessage(serialE);
        ElevatorStateEvent deserializedE = (ElevatorStateEvent) s;

        assertEquals(deserializedE.currentFloor(), 7);
        assertEquals(deserializedE.elevatorNum(), 5);
    }

    @Test
    void DeserializeElevatorStateEventPacketTest() {
        SystemMessage e = new ElevatorStateEvent(5, 7, passengerCountMap);

        byte[] serialE = TransceiverUtility.serializeSystemMessage(e);
        DatagramPacket packet = null;
        try {
            // Host and port not used in test, dummy values used
            packet = new DatagramPacket(serialE, serialE.length, InetAddress.getLocalHost(), 0);
        } catch(UnknownHostException ex){
            ex.printStackTrace();
        }
        
        SystemMessage s = TransceiverUtility.deserializeSystemMessage(packet.getData());
        ElevatorStateEvent deserializedE = (ElevatorStateEvent) s;

        assertEquals(deserializedE.currentFloor(), 7);
        assertEquals(deserializedE.elevatorNum(), 5);
    }

    // New serialize test: compare pre-sent packet get data with post-send get data.
    @Test
    void DeserializePreSendPacketAndPostSendPacket() {

        // SystemMessage e = new ElevatorStateEvent(6, 7, passengerCountMap);
        ArrayList<DestinationEvent> passengers = new ArrayList<>();
        passengers.add(new DestinationEvent(5, Direction.DOWN));
        SystemMessage e = new MovePassengersCommand(6, passengers);

        byte[] serialE = TransceiverUtility.serializeSystemMessage(e);
        DatagramPacket sendPacket = null;

        // Create sendPacket
        try {
            sendPacket = new DatagramPacket(serialE, serialE.length, InetAddress.getLocalHost(), 7500);
        } catch(UnknownHostException ex){
            ex.printStackTrace();
        }

        int length = sendPacket.getData().length;
        System.out.println("length of sendPacket: " + length);
        byte[] send_data = sendPacket.getData();

        // Create receiveSocket
        DatagramSocket receiveSocket = null;
        try {
            receiveSocket = new DatagramSocket(7500);
        } catch (SocketException ex) {
            throw new RuntimeException(ex);
        }

        // Create sendSocket
        DatagramSocket sendSocket = null;
        try {
            sendSocket = new DatagramSocket();
        } catch (SocketException ex) {
            throw new RuntimeException(ex);
        }

        // Send packet
        try {
            sendSocket.send(sendPacket); // TODO: analyze if this corrupts our data
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        // Receive packet
        byte[] data = new byte[507];
        DatagramPacket receivePacket = new DatagramPacket(data, data.length);
        try {
            receiveSocket.receive(receivePacket);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        System.out.println(receivePacket.getData().length);
        assertEquals(send_data, receivePacket.getData());

        // Expected :[B@5db45159
        // Actual   :[B@6107227e

        // SystemMessage s = TransceiverUtility.deserializeSystemMessage(receivePacket.getData());
    }


    @Test
    void MovePassengersCommand() {
        ArrayList<DestinationEvent> passengers = new ArrayList<>();
        passengers.add(new DestinationEvent(5, Direction.DOWN));
        SystemMessage e = new MovePassengersCommand(6, passengers);
        System.out.println(e);
        SystemMessage e2 = new MovePassengersCommand(6, new ArrayList<>(passengers));
        System.out.println(e2);
        passengers.removeFirst();
        System.out.println(e);
        System.out.println(e2);
    }

    @Test
    void ElevatorStateEvent() {
        SystemMessage e = new ElevatorStateEvent(6, 7, passengerCountMap);
        SystemMessage e2 = new ElevatorStateEvent(6, 7, new HashMap<>(passengerCountMap));
        System.out.println(e);
        System.out.println(e2);
        passengerCountMap.clear();
        System.out.println(e);
        System.out.println(e2);
    }

}
