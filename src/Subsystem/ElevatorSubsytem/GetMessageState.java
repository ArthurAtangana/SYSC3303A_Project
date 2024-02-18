package Subsystem.ElevatorSubsytem;

import StatePatternLib.State;

public class GetMessageState extends State<ElevatorContext> {
    public GetMessageState(ElevatorContext context) {
        super(context);
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
