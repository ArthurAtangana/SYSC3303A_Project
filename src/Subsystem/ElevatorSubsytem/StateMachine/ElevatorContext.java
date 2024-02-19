package Subsystem.ElevatorSubsytem.StateMachine;

import Messaging.Direction;
import Messaging.Events.DestinationEvent;
import Messaging.Events.ElevatorStateEvent;
import Messaging.Receivers.DMA_Receiver;
import Messaging.SystemMessage;
import Messaging.Transmitters.DMA_Transmitter;
import Messaging.Transmitters.Transmitter;
import Subsystem.SchedulerSubsystem.SchedulerContext;
import Subsystem.SubsystemContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

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
    private final ArrayList<DestinationEvent> newPassengers;

    public ElevatorContext() {
        super(++key_count);
        txScheduler = new DMA_Transmitter();
        // Initial memory state
        currentFloor = 0; // Start at bottom floor
        passengerCountMap = new HashMap<>(); // Empty at first
        direction = null; // Set before a move command is called
        newPassengers = null;
        // Set state, start it by starting the context in a thread.
        setState(new GetMessageState(this));
    }

    public void bindToScheduler(SchedulerContext sched) {
        // TODO: Replace when UDP receivers are in
        txScheduler.addRx((DMA_Receiver) sched.getRx());
        sched.bindToElevator((DMA_Receiver) rx);
    }

    /**
     * Update scheduler with this elevator's state.
     */
    void sendStateUpdate() {
        ElevatorStateEvent stateEvent = new ElevatorStateEvent(key, currentFloor, passengerCountMap);
        txScheduler.send(stateEvent);
    }

    /**
     * Elevator travels towards buffered direction.
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

    private Direction curPassengerDir() {
        Set<DestinationEvent> destinationEvents = passengerCountMap.keySet();
        // TODO: guard check on every direction being the same.
        Direction direction = null;
        for (DestinationEvent e : destinationEvents) {
            if (direction == null) {
                direction = e.direction();
            }
            if (direction != e.direction()) {
                throw new RuntimeException("Missmatched passenger direction in elevator");
            }
        }
        return direction;
    }


    void storeNewPassengers(ArrayList<DestinationEvent> passengers) {
        newPassengers.addAll(passengers);
    }

    void unload() {
        // GUARD: Skip unload if there are no passengers
        Direction direction = curPassengerDir();
        if (direction == null) {
            return;
        }
        // Unload by removing DestinationEvent mapping (passengers) from passenger count map
        try {
            // each passenger takes loadTime to leave the elevator.
            Thread.sleep(LOAD_TIME * passengerCountMap.remove(new DestinationEvent(currentFloor, direction)));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    void load() {
        // load passengers into the elevator, taking LOAD_TIME per passengers waiting on the floor.
        try {
            Thread.sleep(LOAD_TIME * newPassengers.size());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // for every passenger that board, we add them to the passengerCountMap.
        for (DestinationEvent e : newPassengers) {
            passengerCountMap.merge(e, 1, Integer::sum);
            // if the key exists in passengerCountMap, increment value by 1. if not, add new entry.
        }
    }
}
