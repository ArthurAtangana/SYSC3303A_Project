package Subsystem.ElevatorSubsytem;

import Messaging.Messages.Commands.MoveElevatorCommand;
import Messaging.Messages.Commands.MovePassengersCommand;
import Messaging.Messages.SystemMessage;
import StatePatternLib.Context;
import com.sun.jdi.InvalidTypeException;

/**
 * Class representing the state for an elevator receiving system messages.
 *
 * @author Braeden Kloke
 * @version March 6, 2024
 */
public class ReceivingState extends ElevatorState {
    SystemMessage event;

    public ReceivingState(Context context) {
        super(context);
    }

    @Override
    public void entry() {
        System.out.println("RECEIVING STATE");
        event = ((Elevator) context).receive();
        System.out.println("received event: "+ event);
    }

    @Override
    public void doActivity() {
        if (event instanceof MoveElevatorCommand){
            System.out.println("receiving state to moving");
            context.setNextState(new MovingState(context));
        } else if (event instanceof MovePassengersCommand) {
            System.out.println("receiving state to loading");
            context.setNextState(new LoadingState(context));
        } else {
            InvalidTypeException e = new InvalidTypeException("Event type received cannot be handled by this subsystem.");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handleMoveElevatorCommand() {
        context.changeState(new MovingState(context));
    }

    @Override
    public void handleMovePassengersCommand() {
        context.changeState(new LoadingState(context));
    }
}
