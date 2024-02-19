package Subsystem.ElevatorSubsytem.StateMachine;

import StatePatternLib.State;

public class MoveElevatorState extends State<ElevatorContext> {
    public MoveElevatorState(ElevatorContext context) {
        super(context);
    }

    // OnEntry, OnExit can be open/close doors, respectively

    @Override
    protected void doActivity() {
        context.move();
    }

    /**
     * Algorithm to select next state, can execute event dependant exit code.
     *
     * @return The next state.
     */
    @Override
    protected State<ElevatorContext> selectNextState() {
        return new GetMessageState(context);
    }
}
