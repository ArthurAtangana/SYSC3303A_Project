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

    ElevatorContext() {
        super(++key_count);
        txScheduler = new DMA_Transmitter();
        // Initial memory state
        currentFloor = 0; // Start at bottom floor
        passengerCountMap = new HashMap<>(); // Empty at first
        direction = null; // Set before a move command is called
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
        ElevatorStateEvent stateEvent = new ElevatorStateEvent(key, currentFloor, direction, passengerCountMap);
        txScheduler.send(stateEvent);
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
