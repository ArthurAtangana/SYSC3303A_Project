package Subsystem.SchedulerSubsystem;

import Messaging.Messages.Events.ReceiverBindingEvent;
import StatePatternLib.Context;
import StatePatternLib.State;
import Subsystem.ElevatorSubsytem.Elevator;
import Subsystem.FloorSubsystem.Floor;
import Subsystem.Logging.Logger;
import Subsystem.Subsystem;
import com.sun.jdi.InvalidTypeException;

/**
 * Scheduler FSM State: Binding Receiver State.
 *
 * Class which models the Binding Receiver State of the Scheduler FSM.
 *
 * Responsibilities:
 * - Bind a Receiver to Elevator or Floor Subsystem.
 *
 * @author MD
 * @version Iteration-3
 */
public class BindingReceiverState extends State {

    /* Instance Variables */
    
    // The ReceiverBindingEvent to process
    private final ReceiverBindingEvent event;

    /**
     * Parametric constructor.
     *
     * @param context Context of state machine that this is a state of.
     */
    public BindingReceiverState(Context context, ReceiverBindingEvent event) {
        super(context);
        // Initialize the ReceiverBindingEvent that needs processing in this state.
        this.event = event;
    }

    /**
     * Entry activities for this state.
     *
     * In this state, we just handle Binding of Receivers.
     */
    @Override
    public void entry() {
        String msg = "BindingReceiverState:Entry";
        ((Scheduler)context).logger.log(Logger.LEVEL.DEBUG, ((Scheduler)context).logId, msg);

        Class<? extends Subsystem> subsystemType = event.subsystemType();

        // Case: Subsystem is Elevator
        if (subsystemType.equals(Elevator.class)) {
            ((Scheduler)context).bindElevatorReceiver(event.receiver());
        } 
        // Case: Subsystem is Floor
        else if (subsystemType.equals(Floor.class)) {
            ((Scheduler)context).bindFloorReceiver(event.receiver());
        } 
        // Case: Subsystem is BAD
        else {
            try {
                throw new InvalidTypeException("Unknown subsystem (" + subsystemType + ") attempted to bind to scheduler.");
            } catch (InvalidTypeException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Exit activities for this state.
     */
    @Override
    public void exit() {
        // Next State: BindingReceiverState
        // Required Constructor Arguments: context
        context.setNextState(new ReceivingState(context));
    }
}
