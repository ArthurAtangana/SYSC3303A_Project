package Subsystem.SchedulerSubsystem;

import Messaging.Messages.Commands.MoveElevatorCommand;
import Messaging.Messages.Events.DestinationEvent;
import Messaging.Messages.Events.ElevatorStateEvent;
import Messaging.Messages.Events.FloorRequestEvent;
import Messaging.Messages.Events.ReceiverBindingEvent;
import Messaging.Messages.SystemMessage;
import Messaging.Messages.Commands.SendPassengersCommand;
import StatePatternLib.Context;
import StatePatternLib.State;
import Subsystem.ElevatorSubsytem.Elevator;
import Subsystem.FloorSubsystem.Floor;
import Subsystem.Subsystem;
import com.sun.jdi.InvalidTypeException;

public class BindingReceiverState extends State {

    /* Instance Variables */
    
    // The ReceiverBindingEvent to process
    private ReceiverBindingEvent event;

    /**
     * Parametric constructor.
     *
     * @param context Context of state machine that this is a state of.
     * @author MD
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
        System.out.println("*** Scheduler:BindingReceiverState:Entry");

            Class<? extends Subsystem> subsystemType = event.subsystemType();

            // Case: Subsystem is Elevator
            if (subsystemType.equals(Elevator.class)) {
                ((Scheduler)context).transmitterToElevator.addReceiver(event.receiver());
                System.out.println("--Bound Elevator to Receiver");
            } 
            // Case: Subsystem is Floor
            else if (subsystemType.equals(Floor.class)) {
                ((Scheduler)context).transmitterToFloor.addReceiver(event.receiver());
                System.out.println("--Bound Floor to Receiver");
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
     * Do activities for this state.
     *
     */
    @Override
    public void doActivity() {
        System.out.println("*** Scheduler:BindingReceiverState:Do");

        // Next State: BindingReceiverState
        // Required Constructor Arguments: NA
        context.setNextState(new ReceivingState(context));

    }

    /**
     * Exit activities for this state.
     */
    @Override
    public void exit() {
        System.out.println("*** Scheduler:BindingReceiverState:Exit");
        // Only do this here if exit activities affect next state selection.
        //context.setNextState(new BindingReceiverState(context));
    }

}


