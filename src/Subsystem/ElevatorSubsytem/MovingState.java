package Subsystem.ElevatorSubsytem;

import Messaging.Messages.Commands.MoveElevatorCommand;
import StatePatternLib.Context;

/**
 * Class representing the state for an elevator moving.
 *
 * @author Braeden Kloke
 * @version March 6, 2024
 */
public class MovingState extends ElevatorState {
    public MovingState(Context context) {
        super(context);
    }

    @Override
    public void doActivity() {
        ((Elevator) context).move(((MoveElevatorCommand) context.getEvent()).direction());

        // Once done, transition back to receiving state
        context.changeState(new ReceivingState(context));
    }
}
