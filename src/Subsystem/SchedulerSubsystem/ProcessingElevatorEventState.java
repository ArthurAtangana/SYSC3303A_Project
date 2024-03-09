package Subsystem.SchedulerSubsystem;

import Messaging.Messages.Commands.MoveElevatorCommand;
import Messaging.Messages.Events.DestinationEvent;
import Messaging.Messages.Events.ElevatorStateEvent;
import Messaging.Messages.SystemMessage;
import Messaging.Messages.Commands.SendPassengersCommand;
import StatePatternLib.Context;
import StatePatternLib.State;
import com.sun.jdi.InvalidTypeException;

public class ProcessingElevatorEventState extends State {

    /* Instance Variables */
    
    // The ElevatorStentEvent to process
    private ElevatorStateEvent event;

    /**
     * Parametric constructor.
     *
     * @param context Context of state machine that this is a state of.
     * @author MD
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
        System.out.println("*** Scheduler:ProcessingElevatorEventState:Entry");
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
        System.out.println("*** Scheduler:ProcessingElevatorEventState:Do");

        // Case: Elevator indicates it is Idle.
        // Description: Elevator has neither Floors nor Passengers to service, and is
        //              not in motion. It's just chillin'.
        if (((Scheduler)context).floorRequestsToTime.isEmpty() && event.passengerCountMap().isEmpty()) {

            System.out.printf("Elevator %s idle%n", event.elevatorNum());

            // Register this Elevator as Idle
            ((Scheduler)context).idleElevators.add(event);

            // Next State: ReceivingState
            // Required Constructor Arguments: NA
            context.setNextState(new ReceivingState(context)); 
        } 
        // Case: Elevator indicates it is Stopping.
        // Description: Since the Elevator is stopping, it must be servicing this Floor.
        else if (((Scheduler)context).isElevatorStopping(event)) { 
            System.out.printf("Elevator %s stopped.%n", event.elevatorNum());

            // Notify Floor for service
            ((Scheduler)context).transmitterToFloor.send(new SendPassengersCommand(event.currentFloor(),
                    event.elevatorNum(),
                    ((Scheduler)context).getElevatorDirection(event)));

            // Remove the serviced Floor request
            // NB: Delegate to next state?
            ((Scheduler)context).floorRequestsToTime.remove(new DestinationEvent(event.currentFloor(),((Scheduler)context).getElevatorDirection(event)));

            // Next State: LoadingPassengerState
            // Required Constructor Arguments: NA
            //context.setNextState(new LoadingPassengerState(context)); 
            // CHEAT CODE: Back to RecevingState for now
            context.setNextState(new ReceivingState(context)); 
        }
        // Case: Elevator indicates it is Moving.
        // Description: Keep calm, carry on. It'll be okay. You'll get there buddy.
        else {

            // Request Elevator to keep moving
            ((Scheduler)context).transmitterToElevator.send(new MoveElevatorCommand(event.elevatorNum(), ((Scheduler)context).getElevatorDirection(event)));

            // Next State: ReceivingState
            // Required Constructor Arguments: NA
            context.setNextState(new ReceivingState(context)); 
        }

    }

    /**
     * Exit activities for this state.
     */
    @Override
    public void exit() {
        System.out.println("*** Scheduler:ProcessingElevatorEventState:Exit");
        // Only do this here if exit activities affect next state selection.
        //context.setNextState(new ProcessingElevatorEventState(context));
    }

}


