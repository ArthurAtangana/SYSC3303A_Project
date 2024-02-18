package Subsystem.ElevatorSubsytem.StateMachine;

import Messaging.Commands.MoveElevatorCommand;
import Messaging.SystemMessage;
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
        SystemMessage msg = context.getMessage();
        if (msg instanceof MoveElevatorCommand) {
            context.setDir(((MoveElevatorCommand) msg).direction());
            return new MoveState(context);
        }

        throw new RuntimeException("Message not implemented yet");
    }
}
