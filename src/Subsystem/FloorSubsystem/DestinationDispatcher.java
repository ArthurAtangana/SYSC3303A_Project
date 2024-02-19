/**
 * DestinationDispatcher class sends DestinationEvents as they occur through an input handler.
 * The purpose of this component is to simulate events external to the system occurring in real time
 * by using the time value in InputEvents to determine when to dispatch DestinationEvents.
 *
 * @author Alexandre Marques
 * @version Iteration-1
 */

package Subsystem.FloorSubsystem;

import Messaging.Commands.PassengerArrivedCommand;
import Messaging.Events.DestinationEvent;
import Messaging.Events.FloorInputEvent;
import Messaging.Events.FloorRequestEvent;
import Messaging.Transmitters.Transmitter;
import Messaging.Receivers.DMA_Receiver;
import Messaging.Transmitters.DMA_Transmitter;
import Messaging.Transmitters.Transmitter;
import Subsystem.SchedulerSubsystem.SchedulerContext;

import java.util.ArrayList;
import java.util.Comparator;

public class DestinationDispatcher implements Runnable {
    private long lastEventTime; // Stores occurrence time of last processed event
    private final ArrayList<FloorInputEvent> eventQueue; // Stores occurrence time of last processed event
    private final Transmitter txToScheduler;
    private final Transmitter txToFloor;

    /**
     * The default DestinationDispatcher construtor.
     * TODO: docs
     *
     * @param events The list of input events to dispatch, can be unsorted.
     */
    public DestinationDispatcher(ArrayList<FloorInputEvent> events) {
         this.eventQueue = events;
        this.txToFloor = txToFloor;
        // Sort based on time int (low to high)
         this.eventQueue.sort(Comparator.comparingLong(FloorInputEvent::time));
         // Set baseline time
         lastEventTime = eventQueue.get(0).time();
        txScheduler = new DMA_Transmitter();
    }

    public void bindToScheduler(SchedulerContext sched) {
        // TODO: Replace when UDP receivers are in
        txScheduler.addRx((DMA_Receiver) sched.getRx());
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
        System.out.println("Floor: Waiting for next floor request.");
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
     * Run sequence (loops):
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
            DestinationEvent currentFloorEvent = new DestinationEvent(curEvent.sourceFloor(), curEvent.direction());
            FloorRequestEvent floorReqEvent = new FloorRequestEvent(currentFloorEvent, curEvent.time());

            txToScheduler.send(floorReqEvent);

            // Send passenger destination to floor
            DestinationEvent passengerDestination = new DestinationEvent(curEvent.destinationFloor(), curEvent.direction());
            PassengerArrivedCommand passengerCmd = new PassengerArrivedCommand(curEvent.sourceFloor(), passengerDestination);
            txToFloor.send(passengerCmd);
        }
    }
}
