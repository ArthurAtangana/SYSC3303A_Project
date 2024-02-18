package Subsystem.ElevatorSubsytem;

import Messaging.Direction;
import Messaging.Events.DestinationEvent;
import Messaging.Receivers.Receiver;
import Messaging.Transmitters.Transmitter;
import Subsystem.SubsystemContext;

import java.util.HashMap;

public class ElevatorContext extends SubsystemContext {
    // Unique key generator
    private static int key_count = 0;

    // App logic config
    private static final long TRAVEL_TIME = CONFIG.getTravelTime();
    private static final long LOAD_TIME = CONFIG.getLoadTime();

    // State machine fields for app logic:
    private final Transmitter txScheduler;
    private final int currentFloor;
    private final HashMap<DestinationEvent, Integer> passengerCountMap;

    ElevatorContext(Receiver rx, Transmitter txScheduler) {
        super(++key_count, rx);
        this.txScheduler = txScheduler;
        // Initial memory state
        currentFloor = 0; // Start at bottom floor
        passengerCountMap = new HashMap<>(); // Empty at first
        // Set state, start it by starting the context in a thread.
        setState(new GetMessageState(this));
    }

    /**
     * Elevator travels to a specified floor.
     *
     * @param direction the direction to travel towards.
     */
    private void move(Direction direction) {
        System.out.println("Elevator: Going " + direction + ", from floor #" + currentFloor + ".");
        try {
            Thread.sleep(travelTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        currentFloor += direction.getDisplacement();

        System.out.println("Elevator: Elevator reached floor #" + this.currentFloor + ".");
    }
}
