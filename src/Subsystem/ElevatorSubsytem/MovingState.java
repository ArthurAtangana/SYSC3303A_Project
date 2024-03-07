package Subsystem.ElevatorSubsytem;

import Messaging.Messages.Direction;
import StatePatternLib.Context;
import StatePatternLib.State;

/**
 * Class representing the state for an elevator moving.
 *
 * @author Braeden Kloke
 * @version March 6, 2024
 */
public class MovingState extends State {
    Direction direction;
    public MovingState(Context context, Direction direction) {
        super(context);
        this.direction = direction;
    }

    @Override
    public void entry() {
        System.out.println("MOVING STATE");
    }

    @Override
    public void doActivity() {
        ((Elevator) context).move(direction);
        context.setNextState(new ReceivingState(context));
    }
}