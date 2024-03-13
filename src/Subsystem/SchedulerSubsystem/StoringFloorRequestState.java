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
        //System.out.println("[INFO::FSM] Scheduler:StoringFloorRequestState:Entry");
    }

    /**
     * Do activities for this state.
     *
     * In this state, we just store the FloorRequestEvent, and select next state
     * based on if we have any idle Elevators.
     */
    @Override
    public void doActivity() {
        //System.out.println("[INFO::FSM] Scheduler:StoringFloorRequestState:Do");

        // Store the FloorRequestEvent
        ((Scheduler)context).storeFloorRequest(event);

        // Case: There are idle Elevators.
        // Description: An Elevator is immediately available for work. Find the
        // closest Elevator to the FloorRequest source floor before transitioning.
        if (((Scheduler)context).areIdleElevators()) {
            // Find the closest idle Elevator to the requesting Floor
            ElevatorStateEvent elevatorStateEvent = ((Scheduler)context).getClosestIdleElevator(event);
            // Next State: ProcessingElevatorEventState
            // Required Constructor Arguments: Context, ElevatorStateEvent
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
        //System.out.println("[INFO::FSM] Scheduler:StoringFloorRequestState:Exit");
    }

}
