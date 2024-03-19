package unit.ElevatorSybsystem;

import Messaging.Messages.Direction;
import Messaging.Messages.Events.DestinationEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static Subsystem.ElevatorSubsytem.ElevatorUtilities.getPassengersDirection;
import static org.junit.jupiter.api.Assertions.*;

class ElevatorUtilitiesTest {

    Set<DestinationEvent> passengers;

    @BeforeEach
    void setUp() {
        this.passengers = new HashSet<>();
    }

    /**
     * Asserts that null is returned when no passengers are present.
     */
    @Test
    void noPassengers() {
        Direction direction = getPassengersDirection(passengers);
        assertNull(direction);
    }

    /**
     * Asserts that Direction.UP is returned when all passengers are going up.
     */
    @Test
    void passengersGoingUp() {
        DestinationEvent dest1 = new DestinationEvent(3, Direction.UP, null);
        DestinationEvent dest2 = new DestinationEvent(5, Direction.UP, null);

        passengers.add(dest1);
        passengers.add(dest2);

        Direction direction = getPassengersDirection(passengers);
        assertEquals(Direction.UP, direction);
    }

    /**
     * Asserts that Direction.DOWN is returned when all passengers are going down.
     */
    @Test
    void passengersGoingDown() {
        DestinationEvent dest1 = new DestinationEvent(1, Direction.DOWN, null);
        DestinationEvent dest2 = new DestinationEvent(2, Direction.DOWN, null);

        passengers.add(dest1);
        passengers.add(dest2);

        Direction direction = getPassengersDirection(passengers);
        assertEquals(Direction.DOWN, direction);
    }

    /**
     * Asserts that a RuntimeException is thrown when passengers are going in different directions.
     */
    @Test
    void passengersGoingDifferentDirections() {
        DestinationEvent dest1 = new DestinationEvent(5, Direction.UP, null);
        DestinationEvent dest2 = new DestinationEvent(3, Direction.DOWN, null);

        passengers.add(dest1);
        passengers.add(dest2);

        assertThrows(RuntimeException.class, () -> getPassengersDirection(passengers));
    }

}
