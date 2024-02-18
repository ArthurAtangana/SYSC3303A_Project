package Subsystem.ElevatorSubsytem.StateMachine;

import Messaging.Direction;
import Messaging.Events.DestinationEvent;
import Messaging.Receivers.Receiver;
import Messaging.SystemMessage;
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
    private int currentFloor;
    private final HashMap<DestinationEvent, Integer> passengerCountMap;
    private Direction direction;

    ElevatorContext(Receiver rx, Transmitter txScheduler) {
        super(++key_count, rx);
        this.txScheduler = txScheduler;
        // Initial memory state
        currentFloor = 0; // Start at bottom floor
        passengerCountMap = new HashMap<>(); // Empty at first
        direction = null; // Set before a move command is called
        // Set state, start it by starting the context in a thread.
        setState(new GetMessageState(this));
    }

    /**
     * Elevator travels to a specified floor.
     *
     * @param direction the direction to travel towards.
     */
    void move() {
        System.out.println("Elevator: Going " + direction + ", from floor #" + currentFloor + ".");
        try {
            Thread.sleep(TRAVEL_TIME);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        currentFloor += direction.getDisplacement();
        direction = null; // Reset direction

        System.out.println("Elevator: Elevator reached floor #" + this.currentFloor + ".");
    }

    SystemMessage getMessage() {
        return rx.receive();
    }

    void setDir(Direction dir) {
        direction = dir;
    }
}
