package unit.Networking;

import Messaging.Messages.Direction;
import Messaging.Messages.Events.DestinationEvent;
import Messaging.Messages.Events.ElevatorStateEvent;
import Messaging.Messages.SystemMessage;
import Messaging.Transceivers.TransceiverUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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


}
