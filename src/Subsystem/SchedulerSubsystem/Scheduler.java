package Subsystem.SchedulerSubsystem;

import Messaging.Messages.Commands.MoveElevatorCommand;
import Messaging.Messages.Commands.MovePassengersCommand;
import Messaging.Messages.Commands.SendPassengersCommand;
import Messaging.Messages.Direction;
import Messaging.Messages.Events.*;
import Messaging.Messages.SystemMessage;
import Messaging.Transceivers.Receivers.Receiver;
import Messaging.Transceivers.Transmitters.Transmitter;
import StatePatternLib.Context;
import Subsystem.ElevatorSubsytem.Elevator;
import Subsystem.ElevatorSubsytem.ElevatorUtilities;
import Subsystem.FloorSubsystem.Floor;
import Subsystem.Subsystem;
import com.sun.jdi.InvalidTypeException;

import java.util.*;

/**
 * Scheduler class which models a scheduler in the simulation.
 *
 * @version Iteration-2
 */
public class Scheduler extends Context implements Subsystem {
    private final Transmitter<? extends Receiver> transmitterToFloor;
    private final Transmitter<? extends Receiver> transmitterToElevator;
    private final Receiver receiver;
    private final Map<DestinationEvent, Long> floorRequestsToTime;
    private final ArrayList<ElevatorStateEvent> idleElevators;

    public Scheduler(Receiver receiver,
                     Transmitter<? extends Receiver> transmitterToFloor,
                     Transmitter<? extends Receiver> transmitterToElevator) {
        this.receiver = receiver;
        this.transmitterToElevator = transmitterToElevator;
        this.transmitterToFloor = transmitterToFloor;
        floorRequestsToTime = new HashMap<>();
        idleElevators = new ArrayList<>();

        // Initialize first state for this Subsystem's State Machine
        setNextState(new ReceivingState(this));
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
            processElevatorEvent(idleElevators.remove(0));
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
     * Returns false if the elevator is empty and is moving opposite to the future direction.
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
        if (floorRequestsToTime.isEmpty() && event.passengerCountMap().isEmpty()) {
            System.out.printf("Elevator %s idle%n", event.elevatorNum());
            idleElevators.add(event);
        } else if (isElevatorStopping(event)) { // Stop elevator for a load
            System.out.printf("Elevator %s stopped.%n", event.elevatorNum());
            transmitterToFloor.send(new SendPassengersCommand(event.currentFloor(),
                    event.elevatorNum(),
                    getElevatorDirection(event)));
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
    void processMessage(SystemMessage event) throws InvalidTypeException {
        // Note: Cannot switch on type, if we want to refactor selection, look into visitor pattern.
        // TODO: Refactor with state pattern to clean up this nested mess
        if (event instanceof ElevatorStateEvent esEvent)
            processElevatorEvent(esEvent);
        else if (event instanceof FloorRequestEvent frEvent)
            storeFloorRequest(frEvent);
        else if (event instanceof PassengerLoadEvent plEvent) {
            transmitterToElevator.send(new MovePassengersCommand(plEvent.elevNumber(), plEvent.passengers()));
        } else if (event instanceof ReceiverBindingEvent rbEvent) {
            System.out.println("Bound with: " + rbEvent);
            Class<? extends Subsystem> subsystemType = rbEvent.subsystemType();
            if (subsystemType.equals(Elevator.class)) {
                transmitterToElevator.addReceiver(rbEvent.receiver());
            } else if (subsystemType.equals(Floor.class)) {
                transmitterToFloor.addReceiver(rbEvent.receiver());
            } else
                throw new InvalidTypeException("Unknown subsystem (" + subsystemType +
                        ") attempted to bind to scheduler.");
        }
        else // Default, should never happen
            throw new InvalidTypeException("Event type (" + event.getClass() +
                    ") received cannot be handled by this subsystem.");
    }

    /** 
     * Pop a message from this Subsystem's Receiver buffer.
     */
    SystemMessage receive() {
        return receiver.dequeueMessage();
    }

    /** 
     * Start the State Machine, with initial state of ReceivingState
     */
    @Override
    public void run() {
        while (currentState != null){
            currentState.runState();
        }
    }

}
