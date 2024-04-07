package Subsystem.SchedulerSubsystem;

import Messaging.Messages.Commands.MovePassengersCommand;
import Messaging.Messages.Events.PassengerLoadEvent;
import StatePatternLib.Context;
import StatePatternLib.State;

/**
 * Scheduler FSM State: Loading Passenger State.
 *
 * Class which models the Loading Passenger State of the Scheduler FSM.
 *
 * Responsibilities:
 * - Command an Elevator to load Passengers.
 *
 * @author MD
 * @version Iteration-3
 */
public class LoadingPassengerState extends State {

    /* Instance Variables */
    
    // The PassengerLoadEvent to process
    private final PassengerLoadEvent event;

    /**
     * Parametric constructor.
     *
     * @param context Context of state machine that this is a state of.
     * @author MD
     */
    public LoadingPassengerState(Context context, PassengerLoadEvent event) {
        super(context);
        // Initialize the PassengerLoadEvent that needs processing in this state.
        this.event = event;
    }

    /**
     * Entry activities for this state.
     */
    @Override
    public void entry() {
        String msg = "LoadingPassengerState:Entry";
        ((Scheduler)context).logger.log(Logging.Logger.LEVEL.DEBUG, ((Scheduler)context).logId, msg);
    }

    /**
     * Do activities for this state.
     *
     * In this state, we just transmit a MovePassengersCommand to an Elevator,
     * and then return to Receiving state.
     */
    @Override
    public void doActivity() {
        // Load Passengers, notify Elevator
        MovePassengersCommand movePassengersCommand = new MovePassengersCommand(event.elevNumber(), event.passengers());
        ((Scheduler)context).transmitToElevator(movePassengersCommand);
    }

    /**
     * Exit activities for this state.
     */
    @Override
    public void exit() {
        // Next State: ReceivingState
        // Required Constructor Arguments: context
        context.setNextState(new ReceivingState(context));
    }

}
