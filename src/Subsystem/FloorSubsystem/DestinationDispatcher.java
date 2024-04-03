package Subsystem.FloorSubsystem;

import Messaging.Messages.Commands.PassengerArrivedCommand;
import Messaging.Messages.Events.DestinationEvent;
import Messaging.Messages.Events.FloorInputEvent;
import Messaging.Messages.Events.FloorRequestEvent;
import Messaging.Messages.Events.endSimulationEvent;
import Messaging.Transceivers.Receivers.Receiver;
import Messaging.Transceivers.Transmitters.Transmitter;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * DestinationDispatcher class sends DestinationEvents as they occur through an input handler.
 * The purpose of this component is to simulate events external to the system occurring in real time
 * by using the time value in InputEvents to determine when to dispatch DestinationEvents.
 *
 * @author Alexandre Marques
 * @version Iteration-1
 */
public class DestinationDispatcher implements Runnable {
    private long lastEventTime; // Stores occurrence time of last processed event
    private final ArrayList<FloorInputEvent> eventQueue; // Stores occurrence time of last processed event
    private final Transmitter<? extends Receiver> txToScheduler;
    private final Transmitter<? extends Receiver> txToFloor;

    /**
     * The default DestinationDispatcher construtor.
     * TODO: docs
     *
     * @param events The list of input events to dispatch, can be unsorted.
     */
    public DestinationDispatcher(ArrayList<FloorInputEvent> events,
                                 Transmitter<? extends Receiver> txScheduler,
                                 Transmitter<? extends Receiver> txToFloor) {
        this.txToScheduler = txScheduler;
         this.eventQueue = events;
        this.txToFloor = txToFloor;
        // Sort based on time int (low to high)
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
            DestinationEvent currentFloorEvent = new DestinationEvent(curEvent.sourceFloor(),
                    curEvent.direction(), curEvent.faultType());
            FloorRequestEvent floorReqEvent = new FloorRequestEvent(currentFloorEvent, curEvent.time());

            txToScheduler.send(floorReqEvent);

            // Send passenger destination to floor
            DestinationEvent passengerDestination = new DestinationEvent(curEvent.destinationFloor(),
                    curEvent.direction(), curEvent.faultType());
            PassengerArrivedCommand passengerCmd = new PassengerArrivedCommand(curEvent.sourceFloor(), passengerDestination);
            txToFloor.send(passengerCmd);
        }

        // All FloorInputEvents have been dispatched for this simulation.
        // Send a final message to notify Scheduler.
        txToScheduler.send(new endSimulationEvent("I love this job."));
    }
}
