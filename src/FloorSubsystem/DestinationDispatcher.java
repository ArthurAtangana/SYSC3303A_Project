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

public class DestinationDispatcher implements Runnable {
    private final Parser inputHandler;
    private int lastEventTime;


    DestinationDispatcher(Parser parser){
        inputHandler = parser;
    }

    /**
     * Wait until the next event by converting the numerical time difference
     * between the next event and the previous event into a concrete delay.
     *
     * @param nextEventTime The time until the next event.
     */
    private void waitNextEvent(int nextEventTime){
        // TODO: Implement time to delay conversion based on what feels right.
        //      Waiting mechanism: Thread.sleep? Interrupt?
        lastEventTime = nextEventTime;
    }


    /**
     * Runs this operation.
     */
    @Override
    public void run() {

    }
}
