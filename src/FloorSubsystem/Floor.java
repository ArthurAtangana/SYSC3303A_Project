/**
 * Floor class which models a floor in the simulation.
 *
 * @version 20240202
 */

package FloorSubsystem;

import Networking.Messages.ElevatorStateEvent;
import Networking.Messages.SystemEvent;
import Networking.Receivers.DMA_Receiver;
import com.sun.jdi.InvalidTypeException;

public class Floor implements Runnable {
    private int floorLamp;
    private final int floorNum;
    private final DMA_Receiver receiver;

    public Floor(int floorNumber, DMA_Receiver receiver) {
        this.floorNum = floorNumber;
        this.receiver = receiver;
        // Start elevator location at 0 until an update is received
        floorLamp = 0;
    }
    /**
     * Setting the lamp to display which floor the elevator is on.
     * @param floorNumber The floor number that the elevator is currently on.
     */
    private void setLamp(int floorNumber) {
        floorLamp = floorNumber;
        System.out.println("Floor #"+floorNum+": Lamp display updated to floor#"+ floorLamp + ".");
    }

    /**
     * Process the given event, identify type and select an action or activity based on it.
     *
     * @param event The event to process
     * @throws InvalidTypeException If it receives an event type this class cannot handle
     */
    private void processEvent(SystemEvent event) throws InvalidTypeException {
        // Note: Cannot switch on type, if we want to refactor selection, look into visitor pattern.
        if (event instanceof ElevatorStateEvent)
            setLamp(((ElevatorStateEvent) event).currentFloor());
        else // Default, should never happen
            throw new InvalidTypeException("Event type received cannot be handled by this subsystem.");
    }
    @Override
    public void run() {
        while (true) {
            // receiver.receive = receive state. ProcessEvent "selects" the action
            try {
                processEvent(receiver.receive());
            } catch (InvalidTypeException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
