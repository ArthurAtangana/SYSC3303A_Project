package Subsystem.FloorSubsystem.StateMachine;

import StatePatternLib.State;

public class SendPassengerState extends State<FloorContext> {
    public SendPassengerState(FloorContext context) {
        super(context);
    }

    @Override
    protected void doActivity() {
        context.sendPassengers();
    }

    @Override
    protected void onExit() {
        // Reset state
        context.setLoaderTx(null);
        context.setLoadDir(null);
    }

    /**
     * Algorithm to select next state, can execute event dependant exit code.
     *
     * @return The next state.
     */
    @Override
    protected State<FloorContext> selectNextState() {
        return new GetMessageState(context);
    }
}
