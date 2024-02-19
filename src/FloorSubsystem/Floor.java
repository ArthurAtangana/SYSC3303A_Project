/**
 * Floor class which models a floor in the simulation.
 *
 * @version 20240202
 */

package FloorSubsystem;

import Messaging.Commands.PassengerArrivedCommand;
import Messaging.Commands.SendPassengersCommand;
import Messaging.Direction;
import Messaging.Events.DestinationEvent;
import Messaging.Events.ElevatorStateEvent;
import Messaging.Events.PassengerLoadEvent;
import Messaging.Receivers.DMA_Receiver;
import Messaging.SystemMessage;
import com.sun.jdi.InvalidTypeException;

import java.util.ArrayList;

public class Floor implements Runnable {
    private int floorLamp;
    private final int floorNum;
    private final DMA_Receiver receiver;
    private ArrayList<DestinationEvent> passengers;

    public Floor(int floorNumber, DMA_Receiver receiver) {
        this.floorNum = floorNumber;
        this.receiver = receiver;
        // Start elevator location at 0 until an update is received
        floorLamp = 0;
        passengers = new ArrayList<>();
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
            System.out.println(dest);
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
                processMessage(receiver.receive());
            } catch (InvalidTypeException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
