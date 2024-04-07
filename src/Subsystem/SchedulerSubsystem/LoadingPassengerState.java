package Subsystem.SchedulerSubsystem;

import Messaging.Messages.Commands.MoveElevatorCommand;
import Messaging.Messages.Events.DestinationEvent;
import Messaging.Messages.Events.ElevatorStateEvent;
import Messaging.Messages.Events.FloorRequestEvent;
import Messaging.Messages.Events.PassengerLoadEvent;
import Messaging.Messages.SystemMessage;
import Messaging.Messages.Commands.SendPassengersCommand;
import Messaging.Messages.Commands.MovePassengersCommand;
import StatePatternLib.Context;
import StatePatternLib.State;
import Subsystem.Logging.Logger;
import com.sun.jdi.InvalidTypeException;

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
    private PassengerLoadEvent event;

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
        ((Scheduler)context).logger.log(Logger.LEVEL.DEBUG, ((Scheduler)context).logId, msg);
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