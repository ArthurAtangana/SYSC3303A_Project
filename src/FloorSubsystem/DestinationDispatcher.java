/**
 * DestinationDispatcher class sends DestinationEvents as they occur through an input handler.
 * The purpose of this component is to simulate events external to the system occurring in real time
 * by using the time value in InputEvents to determine when to dispatch DestinationEvents.
 * <p>
 *  Note: currently, the only input handler supported is through text parsing (Parser class),
 *  but this may change in the future so when possible the class is designed around polymorphic input handling classes.
 * </p>
 *
 * @author Alexandre Marques
 * @version 2024-01-30
 */

package FloorSubsystem;

import Networking.Events.DestinationEvent;
import Networking.Events.FloorInputEvent;
import Networking.Transmitters.DMA_Transmitter;

import java.util.ArrayList;
import java.util.Comparator;

public class DestinationDispatcher implements Runnable {
    private long lastEventTime;
    private final ArrayList<FloorInputEvent> eventStack;
    private DMA_Transmitter transmitterToScheduler;


    DestinationDispatcher(Parser inputHandler, DMA_Transmitter transmitter){
        eventStack = inputHandler.getEvents();
        // Sort based on time int (low to high), TODO: TEST THIS
         eventStack.sort(Comparator.comparingLong(FloorInputEvent::time));
        // Set baseline time
         lastEventTime = eventStack.get(0).time();
    }

    /**
     * Wait until the next event by converting the numerical time difference
     * between the next event and the previous event into a concrete delay.
     */
    private void waitEvent(){
        long curTime = eventStack.get(0).time();
        long delay = curTime - lastEventTime;
        System.out.println("Floor subsystem waiting for next event.");
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        lastEventTime = curTime;
    }


    /**
     * Runs this operation.
     */
    @Override
    public void run() {
        while(!eventStack.isEmpty()) {
            waitEvent();
            // Could pop from bottom for performance increase... if that's necessary
            FloorInputEvent curEvent = eventStack.remove(0);
            // Send floor request to scheduler
            transmitterToScheduler.send(new DestinationEvent(curEvent.destinationFloor(), curEvent.direction()));
        }
    }
}
