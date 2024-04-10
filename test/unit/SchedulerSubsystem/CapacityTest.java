package unit.SchedulerSubsystem;

import Messaging.Messages.Direction;
import Messaging.Messages.Events.DestinationEvent;
import Messaging.Messages.Events.ElevatorStateEvent;
import Messaging.Messages.Fault;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static Subsystem.SchedulerSubsystem.Scheduler.getCurCapacity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests current elevator capacity can be determined based on maximum capacity and current passengers onboard.
 */
public class CapacityTest {
    final int MAX_CAPACITY = 3;
    ElevatorStateEvent elevatorStateEvent;
    HashMap<DestinationEvent, Integer> passengerCountMap;
    DestinationEvent d1;
    DestinationEvent d2;

    @BeforeEach
    void setUp() {
        this.d1 = new DestinationEvent(3, Direction.UP, Fault.NONE);
        this.d2 = new DestinationEvent(5, Direction.UP, Fault.NONE);
        this.passengerCountMap = new HashMap<>();
    }

    @Test
    public void testNoCapacity() {
        passengerCountMap.put(d1, 1);
        passengerCountMap.put(d2, 2);
        elevatorStateEvent = new ElevatorStateEvent(1,2, passengerCountMap);

        assertEquals(getCurCapacity(elevatorStateEvent, MAX_CAPACITY), MAX_CAPACITY - 3);
    }

    @Test
    public void testFullCapacity() {
        elevatorStateEvent = new ElevatorStateEvent(1,2, passengerCountMap);

        assertEquals(getCurCapacity(elevatorStateEvent, MAX_CAPACITY), MAX_CAPACITY);
    }

    @Test
    public void testSomeCapacity() {
        passengerCountMap.put(d1, 2);
        elevatorStateEvent = new ElevatorStateEvent(1,2, passengerCountMap);

        assertEquals(getCurCapacity(elevatorStateEvent, MAX_CAPACITY), MAX_CAPACITY - 2);
    }

    @Test
    public void testOverCapacity() {
        passengerCountMap.put(d1, 4);
        elevatorStateEvent = new ElevatorStateEvent(1,2, passengerCountMap);

        assertThrows(RuntimeException.class, () -> getCurCapacity(elevatorStateEvent, MAX_CAPACITY));
    }
}