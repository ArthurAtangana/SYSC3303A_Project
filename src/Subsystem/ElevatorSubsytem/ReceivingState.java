package Subsystem.ElevatorSubsytem;

import Messaging.Messages.Commands.MoveElevatorCommand;
import Messaging.Messages.Commands.MovePassengersCommand;
import Messaging.Messages.SystemMessage;
import StatePatternLib.Context;
import StatePatternLib.State;
import com.sun.jdi.InvalidTypeException;

/**
 * Class representing the state for an elevator receiving system messages.
 *
 * @author Braeden Kloke
 * @version March 6, 2024
 */
public class ReceivingState extends State {
    SystemMessage event;

    public ReceivingState(Context context) {
        super(context);
    }

    @Override
    public void entry() {
        String msg = "ReceivingState:Entry";
        ((Elevator)context).logger.log(Logging.Logger.LEVEL.DEBUG, ((Elevator)context).logId, msg);

        ((Elevator) context).sendStateUpdate();
        event = ((Elevator) context).receive();

        msg = "Received event: " + event;
        ((Elevator)context).logger.log(Logging.Logger.LEVEL.DEBUG, ((Elevator)context).logId, msg);
    }

    @Override
    public void doActivity() {
        if (event instanceof MoveElevatorCommand){
            context.setNextState(new MovingState(context, ((MoveElevatorCommand) event).direction()));
        } else if (event instanceof MovePassengersCommand) {
            context.setNextState(new LoadingState(context, (MovePassengersCommand) event));
        } else {
            InvalidTypeException e = new InvalidTypeException("Event type received cannot be handled by this subsystem.");
            throw new RuntimeException(e);
        }
    }
}
