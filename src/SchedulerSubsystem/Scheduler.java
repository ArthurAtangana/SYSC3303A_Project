package SchedulerSubsystem;

import Networking.Messages.DestinationEvent;
import Networking.Messages.ElevatorStateEvent;
import Networking.Messages.MoveElevatorCommand;
import Networking.Messages.SystemMessage;
import Networking.Receivers.DMA_Receiver;
import Networking.Transmitters.DMA_Transmitter;
import com.sun.jdi.InvalidTypeException;

import java.util.ArrayList;
import java.util.List;

public class Scheduler implements Runnable {
    private final DMA_Transmitter transmitterToFloor;
    private final DMA_Transmitter transmitterToElevator;
    private final DMA_Receiver receiver;
    private List<DestinationEvent> destinationEvents;

    public Scheduler(DMA_Receiver receiver, DMA_Transmitter transmitterToFloor, DMA_Transmitter transmitterToElevator) {
        this.receiver = receiver;
        this.transmitterToElevator = transmitterToElevator;
        this.transmitterToFloor = transmitterToFloor;
        destinationEvents = new ArrayList<DestinationEvent>();
    }

    /**
     * Process event received from the floor.
     *
     * @param destinationEvent Destination event received from floor.
     */
    public void processDestinationEvent(DestinationEvent destinationEvent) {
        // Store event locally to use in scheduling
        destinationEvents.add(destinationEvent);

        // Forward event to elevator subsystem
        // TODO(@alex): describe WHY event is being forwarded to elevator subsystem
        //   HEY ALEX! It would be helpful for the team for you to document HERE why the
        //   elevator subsystem is getting a duplicated event. Much love, Braeden.
        transmitterToElevator.send(destinationEvent);
    }

    /**
     * Check if elevator should stop.
     *
     * @param event Elevator state to check.
     * @return True if elevator should stop, false otherwise.
     */
    public boolean isElevatorStopping(ElevatorStateEvent event) {
        // See state diagram of scheduler for additional context.

        // TODO: implement private function union(destEvents, destEvents) -> destEvents
        List<Integer> destinationFloors = filterDestinationFloors(destinationEvents);

        if (destinationFloors.contains(event.currentFloor())) {
            return true;
        } else {
            return false;
        }
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
     * @param events List of destination events.
     * @return List of destination floors.
     */
    private List<Integer> filterDestinationFloors(List<DestinationEvent> events) {
        List<Integer> destinationFloors = new ArrayList<>();
        for (DestinationEvent e: events) {
            destinationFloors.add(e.destinationFloor());
        }
        return destinationFloors;
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
