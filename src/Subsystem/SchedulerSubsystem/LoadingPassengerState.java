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
import com.sun.jdi.InvalidTypeException;

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
        System.out.println("[INFO::FSM] Scheduler:LoadingPassengerState:Entry");
    }

    /**
     * Do activities for this state.
     *
     * In this state, we just transmit a MovePassengersCommand to an Elevator,
     * and then return to Receiving state (for now, at least).
     */
    @Override
    public void doActivity() {
        System.out.println("[INFO::FSM] Scheduler:LoadingPassengerState:Do");

        // Load Passengers, notify Elevator
        MovePassengersCommand movePassengersCommand = new MovePassengersCommand(event.elevNumber(), event.passengers());
        // TODO: Replace with method call when implemented
        ((Scheduler)context).transmitterToElevator.send(movePassengersCommand);

        // Next State: ReceivingState
        // Required Constructor Arguments: context
        context.setNextState(new ReceivingState(context));
    }

    /**
     * Exit activities for this state.
     */
    @Override
    public void exit() {
        System.out.println("[INFO::FSM] Scheduler:LoadingPassengerState:Exit");
        // Only do this here if exit activities affect next state selection.
        //context.setNextState(new LoadingPassengerState(context));
    }

}
