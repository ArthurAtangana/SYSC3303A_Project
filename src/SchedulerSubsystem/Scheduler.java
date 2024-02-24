package SchedulerSubsystem;

import ElevatorSubsytem.ElevatorUtilities;
import Messaging.Commands.MoveElevatorCommand;
import Messaging.Direction;
import Messaging.Events.DestinationEvent;
import Messaging.Events.ElevatorStateEvent;
import Messaging.Events.FloorRequestEvent;
import Messaging.Receivers.DMA_Receiver;
import Messaging.SystemMessage;
import Messaging.Transmitters.DMA_Transmitter;

import com.sun.jdi.InvalidTypeException;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

/**
 * Scheduler class which models a scheduler in the simulation.
 *
 * @version Iteration-2
 */
public class Scheduler implements Runnable {
    private final DMA_Transmitter transmitterToFloor;
    private final DMA_Transmitter transmitterToElevator;
    private final DMA_Receiver receiver;
    private final Map<DestinationEvent, Long> floorRequestsToTime;
    private final LinkedList<ElevatorStateEvent> idleElevators;

    public Scheduler(DMA_Receiver receiver, DMA_Transmitter transmitterToFloor, DMA_Transmitter transmitterToElevator) {
        this.receiver = receiver;
        this.transmitterToElevator = transmitterToElevator;
        this.transmitterToFloor = transmitterToFloor;
        floorRequestsToTime = new HashMap<>();
        idleElevators = new LinkedList<>();
    }

    /**
     * Process event received from the floor.
     *
     * @param event Event to process.
     */
    private void storeFloorRequest(FloorRequestEvent event) {
        System.out.println("Floor " + event.destinationEvent().destinationFloor() + ": request made: " + event.destinationEvent().direction() + ".");
        // Store event locally to use in scheduling
        if (!floorRequestsToTime.containsKey(event.destinationEvent()))
            // Only store request if it does not already exist
            floorRequestsToTime.put(event.destinationEvent(), event.time());
        if (!idleElevators.isEmpty()){
            processElevatorEvent(idleElevators.removeFirst());
        }
    }
    /**
     * Returns true if elevator should stop, false otherwise.
     *
     * @param e Elevator state to check.
     * @return True if elevator should stop, false otherwise.
     */
    private boolean isElevatorStopping(ElevatorStateEvent e) {
        // Elevator should stop if tuple (elevator.currentFloor, elevator.Direction)
        // belongs to the union of scheduler.destinationEvents and elevator.destinationEvents.
        //
        // See state diagram of scheduler for additional context.

        // Create new object for union set to avoid funny business of destination events
        // being added to the scheduler's floor requests!
        Set<DestinationEvent> union = new HashSet<>();
        union.addAll(floorRequestsToTime.keySet());
        union.addAll(e.passengerCountMap().keySet());

        if (isMovingOppositeToFutureDirection(e)) {
            return false;
        }
        return union.contains(new DestinationEvent(e.currentFloor(), getElevatorDirection(e)));
    }

    /**
     * Returns false if the elevator is empty and is moving oposite to the future direction.
     * @return
     */
    private boolean isMovingOppositeToFutureDirection(ElevatorStateEvent event){
        return (event.passengerCountMap().isEmpty() && getElevatorDirection(event) != getOldestFloorRequest().direction());
    }
    private DestinationEvent getOldestFloorRequest() {
        if (floorRequestsToTime.isEmpty()) return null;
        Long waitTime = Long.MAX_VALUE;
        DestinationEvent oldestFloor = null;
        for (Map.Entry<DestinationEvent, Long> request : floorRequestsToTime.entrySet()) {
            if (request.getValue() < waitTime) {
                waitTime = request.getValue();
                oldestFloor = request.getKey();
            }
        }
        return oldestFloor;
    }
    /**
     * Returns the direction of the oldest floor request based on the elevator's current floor.
     *
     * @param currentFloor The floor number the elevator is currently on.
     * @return Direction of oldest floor request relative to currentFloor, null if there are no floor requests.
     */
    private Direction getDirectionToOldestFloor(int currentFloor) {
        int sourceFloor = getOldestFloorRequest().destinationFloor();
        return (sourceFloor > currentFloor) ? Direction.UP : Direction.DOWN;
    }

    /**
     * Gets the direction an elevator is travelling.
     * Precondition: At least one passenger in the elevator or at least one floor request in floorRequestsToTime.
     *
     * @param event Elevator state to get direction from.
     * @return Direction elevator is travelling.
     */
    private Direction getElevatorDirection(ElevatorStateEvent event) {
        // Find direction in elevator if elevator has passengers.
        Direction direction = ElevatorUtilities.getPassengersDirection(event.passengerCountMap().keySet());
        if (direction != null){
            return direction;
        }
        if (floorRequestsToTime.isEmpty()){
            throw new RuntimeException("No passenger on elevator and no floor requests");
        }
        // If on the same floor as the oldest floor request, return direction of the floor request.
        if (event.currentFloor() == getOldestFloorRequest().destinationFloor()){
            return getOldestFloorRequest().direction();
        }
        // Find direction to oldest floor request.
        return getDirectionToOldestFloor(event.currentFloor());
    }


    /**
     *  process elevator event with elevator state (current floor, direction, passengerList)
     *  and sends it to the floor.
     * @param event an elevator state event
     */
    private void processElevatorEvent(ElevatorStateEvent event) {
        transmitterToFloor.send(event);
        if (floorRequestsToTime.isEmpty() && event.passengerCountMap().isEmpty()) {
            System.out.printf("Elevator %s idle%n", event.elevatorNum());
            idleElevators.add(event);
        } else if (isElevatorStopping(event)) { // Stop elevator for a load
            System.out.printf("Elevator %s stopped.%n", event.elevatorNum());
            new Thread(new Loader(event, transmitterToFloor, transmitterToElevator, getElevatorDirection(event))).start();
            floorRequestsToTime.remove(new DestinationEvent(event.currentFloor(),getElevatorDirection(event)));
        } else// Keep moving
            transmitterToElevator.send(new MoveElevatorCommand(event.elevatorNum(), getElevatorDirection(event)));
    }

    /**
     * Process the given event, identify type and select an action or activity based on it.
     *
     * @param event The event to process
     * @throws InvalidTypeException If it receives an event type this class cannot handle
     */
    private void processMessage(SystemMessage event) throws InvalidTypeException {
        // Note: Cannot switch on type, if we want to refactor selection, look into visitor pattern.
        if (event instanceof ElevatorStateEvent)
            processElevatorEvent((ElevatorStateEvent) event);
        else if (event instanceof FloorRequestEvent)
            storeFloorRequest((FloorRequestEvent) event);
        else // Default, should never happen
            throw new InvalidTypeException("Event type received cannot be handled by this subsystem.");
    }

    @Override
    public void run() {
        while (true){
            try {
                processMessage(receiver.receive());
            } catch (InvalidTypeException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
