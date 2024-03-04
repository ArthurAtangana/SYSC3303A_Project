package FloorSubsystem;

import Messaging.Messages.Commands.PassengerArrivedCommand;
import Messaging.Messages.Commands.SendPassengersCommand;
import Messaging.Messages.Direction;
import Messaging.Messages.Events.DestinationEvent;
import Messaging.Messages.Events.ElevatorStateEvent;
import Messaging.Messages.Events.PassengerLoadEvent;
import Messaging.Messages.SystemMessage;
import Messaging.Transceivers.Receivers.Receiver;
import Messaging.Transceivers.Receivers.ReceiverComposite;
import Messaging.Transceivers.Receivers.ReceiverDMA;
import Messaging.Transceivers.Transmitters.TransmitterDMA;
import com.sun.jdi.InvalidTypeException;

import java.util.ArrayList;

/**
 * Floor class which models a floor in the simulation.
 *
 * @version Iteration-2
 */
public class Floor implements Runnable {
    private static final TransmitterDMA allFloorsDMATransmitter = new TransmitterDMA();

    private int floorLamp;
    private final int floorNum;

    // Receivers
    private final ReceiverComposite receiverComposite; // Groups multiple receivers into 1 queue
    private final Receiver subsystemReceiver;
    private final ReceiverDMA inputEventReceiver;

    private final ArrayList<DestinationEvent> passengers;

    public Floor(int floorNumber, Receiver receiver) {
        this.floorNum = floorNumber;
        // Start elevator location at 0 until an update is received
        floorLamp = 0;
        passengers = new ArrayList<>();
        inputEventReceiver = new ReceiverDMA(floorNumber);
        allFloorsDMATransmitter.addReceiver(inputEventReceiver);
    }

    public static TransmitterDMA getFloorsTransmitter() {
        return allFloorsDMATransmitter;
    }

    /**
     * Setting the lamp to display which floor the elevator is on.
     *
     * @param floorNumber The floor number that the elevator is currently on.
     */
    private void setLamp(int floorNumber) {
        floorLamp = floorNumber;
        System.out.println("Floor #"+floorNum+": Lamp display updated to floor#"+ floorLamp + ".");
    }

    /**
     * Storing the floor request made by a passenger.
     *
     * @param cmd passenger cmd received from the scheduler.
     */
    private void storePassenger(PassengerArrivedCommand cmd) {
        passengers.add(cmd.passenger());
    }

    /**
     * Sending passengers onto the elevator. Once loaded, they are no longer waiting for
     * service from the floor.
     *
     * @param sendPassengersCommand Passenger load event received from the scheduler.
     */
    private void sendPassengers(SendPassengersCommand sendPassengersCommand) {
        ArrayList<DestinationEvent> passengersToLoad = new ArrayList<>();
        Direction currentDirection = sendPassengersCommand.dir();
        // Send passengers with current direction
        for (DestinationEvent dest : passengers) {
            if (dest.direction() == currentDirection) {
                passengersToLoad.add(dest);
            }
        }
        sendPassengersCommand.tx().send(new PassengerLoadEvent(passengersToLoad));
        passengers.removeAll(passengersToLoad);
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
            setLamp(((ElevatorStateEvent) event).currentFloor());
        else if (event instanceof SendPassengersCommand) {
            sendPassengers((SendPassengersCommand) event);
        } else if (event instanceof PassengerArrivedCommand) {
            storePassenger((PassengerArrivedCommand) event);
        }
        else // Default, should never happen
            throw new InvalidTypeException("Event type received cannot be handled by this subsystem.");
    }
    @Override
    public void run() {
        while (true) {
            // receiver.receive = receive state. ProcessEvent "selects" the action
            try {
                processMessage(receiverComposite.dequeueMessage());
            } catch (InvalidTypeException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
