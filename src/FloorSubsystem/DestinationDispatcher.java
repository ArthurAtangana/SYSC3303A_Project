/**
 * DestinationDispatcher class sends DestinationEvents as they occur through an input handler.
 * The purpose of this component is to simulate events external to the system occurring in real time
 * by using the time value in InputEvents to determine when to dispatch DestinationEvents.
 *
 * @author Alexandre Marques
 * @version Iteration-1
 */

package FloorSubsystem;

import Networking.Events.DestinationEvent;
import Networking.Events.FloorInputEvent;
import Networking.Transmitters.DMA_Transmitter;

import java.util.ArrayList;
import java.util.Comparator;

public class DestinationDispatcher implements Runnable {
    private long lastEventTime; // Stores occurrence time of last processed event
    private final ArrayList<FloorInputEvent> eventQueue; // Stores occurrence time of last processed event
    private final DMA_Transmitter transmitterToScheduler;

    /**
     * The default DestinationDispatcher construtor.
     *
     * @param events The list of input events to dispatch, can be unsorted.
     * @param transmitterToScheduler A transmitter sending to a scheduler, to dispatch the events to it.
     */
    public DestinationDispatcher(ArrayList<FloorInputEvent> events, DMA_Transmitter transmitterToScheduler){
         this.transmitterToScheduler = transmitterToScheduler;
         this.eventQueue = events;
         // Sort based on time int (low to high), TODO: TEST THIS
         this.eventQueue.sort(Comparator.comparingLong(FloorInputEvent::time));
         // Set baseline time
         lastEventTime = eventQueue.get(0).time();
    }

    /**
     * Wait until the next event by converting the numerical time difference
     * between the next event and the previous event into a concrete delay.
     *
     * Note: Because the wait time is entirely encapsulated here (we're not using a system clock),
     * the precessing time spent outside this method is not factored in.
     */
    private void waitEvent(){
        long curTime = eventQueue.get(0).time();
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
     * Dispatcher runs until there are no events left to be sent.
     *
     * Run state machine:
     * 1. Wait for next event time
     * 2. Get next FloorInputEvent
     * 3. Parse FloorInputEvent, create DestinationEvent
     * 4. Send destinations to appropriate receivers.
     * 5. Exit IFF no events left.
     */
    @Override
    public void run() {
        while(!eventQueue.isEmpty()) {
            waitEvent();
            // Could pop from bottom for performance increase... if that's necessary
            FloorInputEvent curEvent = eventQueue.remove(0);
            // Send floor request to scheduler
            transmitterToScheduler.send(new DestinationEvent(curEvent.destinationFloor(), curEvent.direction()));
        }
    }
}
