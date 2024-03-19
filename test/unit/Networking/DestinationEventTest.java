package unit.Networking;

import Messaging.Messages.Direction;
import Messaging.Messages.Events.DestinationEvent;
import Messaging.Messages.Fault;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class DestinationEventTest {
    @Test
    void testEqualsNormalFields() {
        DestinationEvent deIni = new DestinationEvent(1, Direction.UP, null);
        DestinationEvent deSame = new DestinationEvent(1, Direction.UP, null);
        DestinationEvent deDiffDir = new DestinationEvent(1, Direction.DOWN, null);
        DestinationEvent deDiffFloor = new DestinationEvent(2, Direction.UP, null);

        assertEquals(deIni, deIni); // Same object
        assertEquals(deIni, deSame); // Diff object, same fields
        assertNotEquals(deIni, deDiffDir); // Different direction field
        assertNotEquals(deIni, deDiffFloor); // Different floor field

    }

    @Test
    void testEqualsFault() {
        DestinationEvent deIni = new DestinationEvent(1, Direction.UP, Fault.NONE);
        DestinationEvent deWildFault = new DestinationEvent(1, Direction.UP, null);
        DestinationEvent deDiffFault = new DestinationEvent(1, Direction.UP, Fault.HARD);

        assertEquals(deIni, deWildFault); // Null fault acts as wildcard
        assertNotEquals(deIni, deDiffFault); // Different concrete fault should not be equal
    }

}