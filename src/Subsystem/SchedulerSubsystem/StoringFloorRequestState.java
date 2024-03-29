package Subsystem.SchedulerSubsystem;

import Messaging.Messages.Commands.MoveElevatorCommand;
import Messaging.Messages.Events.DestinationEvent;
import Messaging.Messages.Events.ElevatorStateEvent;
import Messaging.Messages.Events.FloorRequestEvent;
import Messaging.Messages.SystemMessage;
import Messaging.Messages.Commands.SendPassengersCommand;
import StatePatternLib.Context;
import StatePatternLib.State;
import com.sun.jdi.InvalidTypeException;

/**
 * Scheduler FSM State: Storing Floor Request State.
 *
 * Class which models the Storing Floor Request State of the Scheduler FSM.
 *
 * Responsibilities:
 * - Store a a request for floor service
 * - If there are idle elevators, make them available for immediate service
 *
 * @author MD
 * @version Iteration-3
 */
public class StoringFloorRequestState extends State {

    /* Instance Variables */
    
    // The FloorRequestEvent to process
    private FloorRequestEvent event;

    /**
     * Parametric constructor.
     *
     * @param context Context of state machine that this is a state of.
     */
    public StoringFloorRequestState(Context context, FloorRequestEvent event) {
        super(context);
        // Initialize the ElevatorStateEvent that needs processing in this state.
        this.event = event;
    }

    /**
     * Entry activities for this state.
     */
    @Override
    public void entry() {
        String msg = "StoringFloorRequestState:Entry";
        ((Scheduler)context).logger.log(Logging.Logger.LEVEL.DEBUG, ((Scheduler)context).logId, msg);
    }

    /**
     * Do activities for this state.
     *
     * In this state, we just store the FloorRequestEvent, and select next state
     * based on if we have any idle Elevators.
     */
    @Override
    public void doActivity() {

        // Store the FloorRequestEvent
        ((Scheduler)context).storeFloorRequest(event);

        // Case: There are idle Elevators.
        // Description: An Elevator is immediately available for work.
        if (((Scheduler)context).areIdleElevators()) {
            // Next State: ProcessingElevatorEventState
            // Required Constructor Arguments: ElevatorStateEvent
            ElevatorStateEvent elevatorStateEvent = ((Scheduler)context).getClosestIdleElevator(event);
            context.setNextState(new ProcessingElevatorEventState(context, elevatorStateEvent)); 
        }
        // Case: There are NO idle Elevators.
        // Description: No Elevator is immediately available for work.
        else {
            // Next State: ReceivingState
            // Required Constructor Arguments: context
            context.setNextState(new ReceivingState(context)); 
        }

    }

    /**
     * Exit activities for this state.
     */
    @Override
    public void exit() {
    }

}
