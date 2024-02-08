/**
 * Elevator class which models an elevator in the simulation.
 *
 * @version 20240202
 */

package ElevatorSubsytem;

import Networking.Direction;
import Networking.Messages.DestinationEvent;
import Networking.Messages.ElevatorStateEvent;
import Networking.Messages.SystemEvent;
import Networking.Receivers.DMA_Receiver;
import Networking.Transmitters.DMA_Transmitter;
import com.sun.jdi.InvalidTypeException;

import java.util.ArrayList;

public class Elevator implements Runnable {
    /** Single floor travel time */
    public final long TRAVEL_TIME = 8500;
    private int currentFloor;
    private Direction direction;
    private final ArrayList<DestinationEvent> passengerDestinations;
    private final DMA_Transmitter transmitterToScheduler;
    private final DMA_Receiver receiver;

    public Elevator(DMA_Receiver receiver, DMA_Transmitter transmitter) {
        this.currentFloor = 0;
        this.direction = Direction.STOPPED;
        this.passengerDestinations = new ArrayList<>();
        this.transmitterToScheduler = transmitter;
        this.receiver = receiver;
    }

    /**
     * Elevator travels to a specified floor.
     * @param event the DestinationEvent containing the information.
     */
    @Deprecated
    // TODO: To be replaced by single floor travel function
    private void travelToFloor(DestinationEvent event) {
        int floorNumber = event.destinationFloor();
        direction = event.direction();

        System.out.println("Elevator: Going " + direction + ", to floor" + floorNumber + ".");
        try {
            // TODO: multiply travel time by num floor in future iterations (refine math)
            Thread.sleep(TRAVEL_TIME);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.currentFloor = floorNumber;
        System.out.println("Elevator: Elevator is on floor " + this.currentFloor + ".");
    }

    /**
     * Update scheduler with this elevator's state.
     */
    private void sendStateUpdate(){
        ElevatorStateEvent stateEvent = new ElevatorStateEvent(currentFloor, direction, passengerDestinations);
        transmitterToScheduler.send(stateEvent);
    }

    /**
     * Process the given event, identify type and select an action or activity based on it.
     *
     * @param event The event to process
     * @throws InvalidTypeException If it receives an event type this class cannot handle
     */
    private void processEvent(SystemEvent event) throws InvalidTypeException {
        // Note: Cannot switch on type, if we want to refactor selection, look into visitor pattern.
        if (event instanceof DestinationEvent)
            travelToFloor(((DestinationEvent) event));
        else // Default, should never happen
            throw new InvalidTypeException("Event type received cannot be handled by this subsystem.");
    }

    @Override
    public void run() {
        while (true){
            try {
                processEvent(receiver.receive());
            } catch (InvalidTypeException e) {
                throw new RuntimeException(e);
            }
            sendStateUpdate();
        }
    }
}
