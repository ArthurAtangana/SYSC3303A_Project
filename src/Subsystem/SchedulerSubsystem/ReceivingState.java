package Subsystem.SchedulerSubsystem;

import Messaging.Messages.Events.*;
import Messaging.Messages.SystemMessage;
import StatePatternLib.Context;
import StatePatternLib.State;
import com.sun.jdi.InvalidTypeException;

/**
 * Scheduler FSM State: Receiving State.
 *
 * Class which models the Receiving State of the Scheduler FSM.
 *
 * Responsibilities:
 * - Receive a SystemMessage event from the Scheduler's Receiver
 * - Point to the appropriate next state and pass it the received event
 *
 * @author AA/MD
 * @version Iteration-3
 */
public class ReceivingState extends State {

    /* Instance Variables */
    
    // The event to receive.
    private SystemMessage event;

    /**
     * Parametric constructor.
     *
     * @param context Context of state machine that this is a state of.
     */
    public ReceivingState(Context context) {
        super(context);
    }

    /**
     * Entry activities for this state.
     */
    @Override
    public void entry() {
        String msg = "ReceivingState:Entry";
        ((Scheduler)context).logger.log(Logging.Logger.LEVEL.DEBUG, ((Scheduler)context).logId, msg);

        if (!((Scheduler) context).isEndOfSimulation()) {
            // Simulation has not ended, keep receiving events!

            // Get the SystemMessage (pop from Receiver buffer)
            event = ((Scheduler) context).receive();
        }
    }

    
    /**
     * Do activities for this state.
     *
     * In this state, we simply switch on the received SystemMessage event, and point 
     * to the next state.
     */
    @Override
    public void doActivity() {

        String msg = "";

        // Case: Simulation has ended
        // Description: Let's get outta here.
        if (((Scheduler)context).isEndOfSimulation()) {
            msg = "Simulation ended.";
            ((Scheduler) context).logger.log(Logging.Logger.LEVEL.DEBUG, ((Scheduler) context).logId, msg);
            ((Scheduler)context).setSimulationEndTime();
            context.setNextState(new FinalState(context));
        }
        // Case: Event is ElevatorStateEvent
        // Description: Notification from Elevator conveying its state
        else if (event instanceof ElevatorStateEvent esEvent) {
            msg = "Received ElevatorStateEvent.";
            ((Scheduler)context).logger.log(Logging.Logger.LEVEL.DEBUG, ((Scheduler)context).logId, msg);
            // Next State: ProcessingElevatorEventState
            // Required Constructor Arguments: context
            context.setNextState(new ProcessingElevatorEventState(context, esEvent)); 
        }
        // Case: Event is FloorRequestEvent
        // Description: Request from Floor asking for service
        else if (event instanceof FloorRequestEvent frEvent) {
            msg = "Received FloorRequestEvent.";
            ((Scheduler)context).logger.log(Logging.Logger.LEVEL.DEBUG, ((Scheduler)context).logId, msg);
            // Next State: StoringFloorRequestState
            // Required Constructor Arguments: context
            context.setNextState(new StoringFloorRequestState(context, frEvent)); 
        }
        // Case: Event is PassengerLoadEvent
        // Description: Notification from Floor of Passengers requiring load
        else if (event instanceof PassengerLoadEvent plEvent) {
            msg = "Received PassengerLoadEvent.";
            ((Scheduler)context).logger.log(Logging.Logger.LEVEL.DEBUG, ((Scheduler)context).logId, msg);
            // Next State: LoadingPassengerState
            // Required Constructor Arguments: PassengerLoadEvent
            context.setNextState(new LoadingPassengerState(context, plEvent));
        } 
        // Case: Event is ReceiverBindingEvent
        // Description: Request to bind a Receiver to this Scheduler Subsystem
        else if (event instanceof ReceiverBindingEvent rbEvent) {
            msg = "Received ReceiverBindingEvent.";
            ((Scheduler)context).logger.log(Logging.Logger.LEVEL.DEBUG, ((Scheduler)context).logId, msg);
            // Next State: BindingReceiverState
            // Required Constructor Arguments: ReceiverBindingEvent
            context.setNextState(new BindingReceiverState(context, rbEvent));
        }
        // Case: Event is StartSimulationEvent
        // Description: Let's go.
        else if (event instanceof StartSimulationEvent) {
            msg = "Received event to start simulation ... recording start time.";
            ((Scheduler)context).logger.log(Logging.Logger.LEVEL.DEBUG, ((Scheduler)context).logId, msg);
            ((Scheduler)context).setSimulationStartTime();
        }
        // Case: Event is EndSimulationEvent
        // Description: End it all.
        else if (event instanceof EndSimulationEvent) {
            msg = "Received event to end simulation ... setting flag.";
            ((Scheduler)context).logger.log(Logging.Logger.LEVEL.DEBUG, ((Scheduler)context).logId, msg);
            ((Scheduler)context).setSimulationEnding(); // Flag set. Nailed it!
            context.setNextState(new ReceivingState(context)); // Go back to receiving ...
        }
        else {
            InvalidTypeException e = new InvalidTypeException("Event type received cannot be handled by this subsystem.");
            throw new RuntimeException(e);
        }

    }

    /**
     * Exit activities for this state.
     */
    @Override
    public void exit() {}
}
