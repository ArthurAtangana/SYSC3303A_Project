package SchedulerSubsystem;

import Networking.Messages.DestinationEvent;
import Networking.Messages.ElevatorStateEvent;
import Networking.Messages.MoveElevatorCommand;
import Networking.Messages.SystemMessage;
import Networking.Receivers.DMA_Receiver;
import Networking.Transmitters.DMA_Transmitter;
import com.sun.jdi.InvalidTypeException;

import java.util.*;

public class Scheduler implements Runnable {
    private final DMA_Transmitter transmitterToFloor;
    private final DMA_Transmitter transmitterToElevator;
    private final DMA_Receiver receiver;
    private Set<DestinationEvent> destinationEvents;

    public Scheduler(DMA_Receiver receiver, DMA_Transmitter transmitterToFloor, DMA_Transmitter transmitterToElevator) {
        this.receiver = receiver;
        this.transmitterToElevator = transmitterToElevator;
        this.transmitterToFloor = transmitterToFloor;
        destinationEvents = new HashSet<DestinationEvent>();
    }

    /**
     * Process event received from the floor.
     *
     * @param destinationEvent Destination event received from floor.
     */
    private void processDestinationEvent(DestinationEvent destinationEvent) {
        // Store event locally to use in scheduling
        destinationEvents.add(destinationEvent);
    }

    /**
     * Returns true if elevator should stop, false otherwise.
     *
     * @param e Elevator state to check.
     * @return True if elevator should stop, false otherwise.
     */
    public boolean isElevatorStopping(ElevatorStateEvent e) {
        // Elevator should stop if elevator.currentFloor belongs to the
        // union of scheduler.destinationEvents and elevator.destinationEvents.
        //
        // See state diagram of scheduler for additional context.

        Set<DestinationEvent> union = unionOfDestinationEvents(destinationEvents, e.destinationEvents());
        Set<Integer> destinationFloors = filterDestinationFloors(union);
        return destinationFloors.contains(e.currentFloor());
    }

    /**
     *  process elevator event with elevator state (current floor, direction, passengerList)
     *  and sends it to the floor.
     * @param event an elevator state event
     */
    private void processElevatorEvent(ElevatorStateEvent event) {
        if (isElevatorStopping(event)) // Stop elevator for a load
            new Thread(new Loader(event, transmitterToFloor, transmitterToElevator)).start();
        else // Keep moving
            transmitterToElevator.send(new MoveElevatorCommand(event.elevatorNum()));
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
        else if (event instanceof DestinationEvent)
            processDestinationEvent((DestinationEvent) event);
        else // Default, should never happen
            throw new InvalidTypeException("Event type received cannot be handled by this subsystem.");
    }

    /**
     * Filters destination floors from destination events.
     *
     * @param events Set of destination events.
     * @return Set of destination floors.
     */
    private Set<Integer> filterDestinationFloors(Set<DestinationEvent> events) {
        Set<Integer> destinationFloors = new HashSet<>();
        for (DestinationEvent e: events) {
            destinationFloors.add(e.destinationFloor());
        }
        return destinationFloors;
    }

    /**
     * Takes union of two destination event sets.
     *
     * @param A Set of destination events.
     * @param B Set of destination events.
     * @return Union of both sets.
     */
    private Set<DestinationEvent> unionOfDestinationEvents(
            Collection<DestinationEvent> A,
            Collection<DestinationEvent> B) {
        HashSet<DestinationEvent> union = new HashSet<DestinationEvent>();
        for (DestinationEvent a: A) {
            if (B.contains(a)) {
                union.add(a);
            }
        }
        return union;
    }

    /**
     * Sets destination events of this scheduler.
     *
     * @param events Set of destination events to be set.
     */
    public void setDestinationEvents(Set<DestinationEvent> events) {
        // Documenting a setter for a set is unsettling ...
        this.destinationEvents = events;
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
