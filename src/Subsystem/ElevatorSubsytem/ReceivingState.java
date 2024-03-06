package Subsystem.ElevatorSubsytem;

import StatePatternLib.Context;

/**
 * Class representing the state for an elevator receiving system messages.
 *
 * @author Braeden Kloke
 * @version March 6, 2024
 */
public class ReceivingState extends ElevatorState {

    public ReceivingState(Context context) {
        super(context);
    }

    @Override
    public void entry() {
        Elevator elevator = (Elevator) this.context;
        elevator.sendStateUpdate();
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
