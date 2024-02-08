package SchedulerSubsystem;

import Networking.Messages.DestinationEvent;
import Networking.Messages.ElevatorStateEvent;
import Networking.Messages.MoveElevatorCommand;
import Networking.Messages.SystemMessage;
import Networking.Receivers.DMA_Receiver;
import Networking.Transmitters.DMA_Transmitter;
import com.sun.jdi.InvalidTypeException;

public class Scheduler implements Runnable {
    private final DMA_Transmitter transmitterToFloor;
    private final DMA_Transmitter transmitterToElevator;
    private final DMA_Receiver receiver;

    public Scheduler(DMA_Receiver receiver, DMA_Transmitter transmitterToFloor, DMA_Transmitter transmitterToElevator) {
        this.receiver = receiver;
        this.transmitterToElevator = transmitterToElevator;
        this.transmitterToFloor = transmitterToFloor;
    }

    /**
     * process event received from the floor (up or down request)
     * and sends it to the elevator.
     * @param destinationEvent a destination event
     */
    private void processDestinationEvent(DestinationEvent destinationEvent) {
        transmitterToElevator.send(destinationEvent);
    }
    /**
     *  process elevator event with elevator state (current floor, direction, passengerList)
     *  and sends it to the floor.
     * @param elevatorStateEvent an elevator state event
     */
    private void processElevatorEvent(ElevatorStateEvent elevatorStateEvent) {
        transmitterToFloor.send(elevatorStateEvent);
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
