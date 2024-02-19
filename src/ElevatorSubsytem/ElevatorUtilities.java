package ElevatorSubsytem;

import Messaging.Direction;
import Messaging.Events.DestinationEvent;
import Messaging.Events.FloorRequestEvent;

import java.util.Set;


public class ElevatorUtilities {

    /**
     * Get the direction of the passengers in the elevator.
     * @param passengers
     * @return the direction of the elevator (UP, DOWN, null)
     */
    public static Direction getPassengersDirection(Set<DestinationEvent> passengers) {
        // Find direction in elevator if elevator has passengers.
        Direction direction = null;
        for (DestinationEvent e : passengers) {
            if (direction == null) {
                direction = e.direction();
            }
            if (direction != e.direction()) {
                throw new RuntimeException("Mismatched passenger direction in elevator");
            }
        }
        return direction;
    }

}
