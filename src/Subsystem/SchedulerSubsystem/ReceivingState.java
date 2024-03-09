package Subsystem.SchedulerSubsystem;

import Messaging.Messages.Events.ElevatorStateEvent;
import Messaging.Messages.Events.FloorRequestEvent;
import Messaging.Messages.Events.PassengerLoadEvent;
import Messaging.Messages.Events.ReceiverBindingEvent;
import Messaging.Messages.SystemMessage;
import StatePatternLib.Context;
import StatePatternLib.State;
import com.sun.jdi.InvalidTypeException;

public class ReceivingState extends State {

    /* Instance Variables */
    
    // The event to receive.
    private SystemMessage event;

    /**
     * Parametric constructor.
     *
     * @param context Context of state machine that this is a state of.
     * @author AA/MD
     */
    public ReceivingState(Context context) {
        super(context);
    }

    /**
     * Entry activities for this state.
     */
    @Override
    public void entry() {
        System.out.println("*** Scheduler:ReceivingState:Entry");
        // Get the SystemMessage (pop from Receiver buffer)
        event = ((Scheduler) context).receive();
    }

    
    /**
     * Do activities for this state.
     *
     * In this state, we simply switch on the received SystemMessage event, and point 
     * to the next state.
     */
    @Override
    public void doActivity() {
        System.out.println("*** Scheduler:ReceivingState:Do");

        // Case: Event is ElevatorStateEvent
        // Description: Notification from Elevator conveying its state
        if (event instanceof ElevatorStateEvent esEvent) {
            System.out.println("*** Scheduler:ReceivingState:Entry: Received ElevatorStateEvent.");
            // Next State: ProcessingElevatorEventState
            // Required Constructor Arguments: NA
            context.setNextState(new ProcessingElevatorEventState(context, esEvent)); 
        }
        // Case: Event is FloorRequestEvent
        // Description: Request from Floor asking for service
        else if (event instanceof FloorRequestEvent frEvent) {
            System.out.println("*** Scheduler:ReceivingState:Entry: Received FloorRequestEvent.");
            // Next State: StoringFloorRequestState
            // Required Constructor Arguments: NA
            context.setNextState(new StoringFloorRequestState(context, frEvent)); 
            // CHEAT CODE: Back to ReceivingState for now
            //context.setNextState(new ReceivingState(context)); 
        }
        // Case: Event is PassengerLoadEvent
        // Description: Notification from Floor of Passengers requiring load
        else if (event instanceof PassengerLoadEvent plEvent) {
            System.out.println("*** Scheduler:ReceivingState:Entry: Received PassengerLoadEvent.");
            // Next State: LoadingPassengerState
            // Required Constructor Arguments: PassengerLoadEvent
            context.setNextState(new LoadingPassengerState(context, plEvent));
            // CHEAT CODE: Back to ReceivingState for now
            //context.setNextState(new ReceivingState(context)); 
        } 
        // Case: Event is ReceiverBindingEvent
        // Description: Request to bind a Receiver to this Scheduler Subsystem
        else if (event instanceof ReceiverBindingEvent rbEvent) {
            System.out.println("*** Scheduler:ReceivingState:Entry: Received ReceiverBindingEvent.");
            // Next State: BindingReceiverState
            // Required Constructor Arguments: ReceiverBindingEvent
            context.setNextState(new BindingReceiverState(context, rbEvent));
            // CHEAT CODE: Back to ReceivingState for now
            //context.setNextState(new ReceivingState(context)); 
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
    public void exit() {
        System.out.println("*** Scheduler:ReceivingState:Exit\n");
        // Only do this here if exit activities affect next state selection.
        //context.setNextState(new ReceivingState(context));
    }

}


