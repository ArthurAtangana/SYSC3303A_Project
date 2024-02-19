package Subsystem.ElevatorSubsytem.StateMachine;

import StatePatternLib.State;

public class MovePassengersState extends State<ElevatorContext> {
    public MovePassengersState(ElevatorContext context) {
        super(context);
    }

    @Override
    protected void doActivity() {
        context.unload();
        context.load();
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
