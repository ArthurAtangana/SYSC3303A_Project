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

import java.util.ArrayList;
import java.util.Comparator;

public class DestinationDispatcher implements Runnable {
    private final ArrayList<InputEvent> eventStack;
    private int lastEventTime;


    DestinationDispatcher(Parser inputHandler){
        eventStack = inputHandler.getEvents();
        // Sort based on time int (low to high), TODO: TEST THIS
         eventStack.sort(Comparator.comparingInt(InputEvent::time));
        // Set baseline time
         lastEventTime = eventStack.get(0).time();
    }

    /**
     * Wait until the next event by converting the numerical time difference
     * between the next event and the previous event into a concrete delay.
     */
    private void waitEvent(){
        int curTime = eventStack.get(0).time();
        int delay = curTime - lastEventTime;
        // TODO: Implement time to delay conversion based on what feels right.
        //      Waiting mechanism: Thread.sleep? Interrupt?
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
            InputEvent curEvent = eventStack.remove(0);
            // TODO: Dispatch event;
            //  --> Q how to select floor? How to route across multiple floors?
            //  --> Q how to select Scheduler? Singleton?
        }
    }
}
