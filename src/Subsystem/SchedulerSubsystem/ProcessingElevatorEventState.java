package Subsystem.SchedulerSubsystem;

import Messaging.Messages.Commands.MoveElevatorCommand;
import Messaging.Messages.Events.DestinationEvent;
import Messaging.Messages.Events.ElevatorStateEvent;
import Messaging.Messages.Events.PassengerLoadEvent;
import Messaging.Messages.SystemMessage;
import Messaging.Messages.Commands.SendPassengersCommand;
import StatePatternLib.Context;
import StatePatternLib.State;
import com.sun.jdi.InvalidTypeException;

/**
 * Scheduler FSM State: Processing Elevator Event State.
 *
 * Class which models the Processing Elevator Event State of the Scheduler FSM.
 *
 * Responsibilities:
 * - Determine if Elevator is in service or is idle
 * - Initiate service if Elevator is stopping at a Floor
 * - Keep the Elevator moving if does not need to stop at a Floor
 *
 * @author MD
 * @version Iteration-3
 */
public class ProcessingElevatorEventState extends State {

    /* Instance Variables */
    
    // The ElevatorStentEvent to process
    private ElevatorStateEvent event;

    /**
     * Parametric constructor.
     *
     * @param context Context of state machine that this is a state of.
     */
    public ProcessingElevatorEventState(Context context, ElevatorStateEvent event) {
        super(context);
        // Initialize the ElevatorStateEvent that needs processing in this state.
        this.event = event;
    }

    /**
     * Entry activities for this state.
     */
    @Override
    public void entry() {
        System.out.println("[INFO::FSM] Scheduler:ProcessingElevatorEventState:Entry");
    }

    /**
     * Do activities for this state.
     *
     * In this state, we process an ElevatorStateEvent received from an Elevator,
     * which indicates its state, and then determine the apopropriate action for
     * that Elevator to take.
     */
    @Override
    public void doActivity() {
        System.out.println("[INFO::FSM] Scheduler:ProcessingElevatorEventState:Do");

        // Case: Elevator indicates it is Idle.
        // Description: Elevator has neither Floors nor Passengers to service, and is
        //              not in motion. It's just chillin'.
        if (!((Scheduler)context).hasPendingDestinationEvents() && event.passengerCountMap().isEmpty()) {

            System.out.printf("Scheduler: Elevator %s idle%n", event.elevatorNum());

            // Register this Elevator as Idle
            ((Scheduler)context).addIdleElevator(event);

            // Next State: ReceivingState
            // Required Constructor Arguments: context
            context.setNextState(new ReceivingState(context)); 
        } 
        // Case: Elevator indicates it is Stopping.
        // Description: Since the Elevator is stopping, it must be servicing this Floor.
        else if (((Scheduler)context).isElevatorStopping(event)) { 
            System.out.printf("Elevator %s stopped.%n", event.elevatorNum());

            // Notify Floor for service
            ((Scheduler)context).transmitToFloor(new SendPassengersCommand(event.currentFloor(), event.elevatorNum(), ((Scheduler)context).getElevatorDirection(event)));

            // Remove the serviced DestinationRequest request
            ((Scheduler)context).removeDestinationEvent(new DestinationEvent(event.currentFloor(),((Scheduler)context).getElevatorDirection(event)));

            // Next State: ReceivingState
            // Required Constructor Arguments: context
            context.setNextState(new ReceivingState(context)); 

        }
        // Case: Elevator indicates it is Moving.
        // Description: Keep calm, carry on. It'll be okay. You'll get there buddy.
        else {

            // Request Elevator to keep moving
            ((Scheduler)context).transmitToElevator(new MoveElevatorCommand(event.elevatorNum(), ((Scheduler)context).getElevatorDirection(event)));

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
        System.out.println("[INFO::FSM] Scheduler:ProcessingElevatorEventState:Exit");
    }

}
