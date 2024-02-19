package Subsystem.ElevatorSubsytem.StateMachine;

import Messaging.Commands.MoveElevatorCommand;
import Messaging.Commands.MovePassengersCommand;
import Messaging.SystemMessage;
import StatePatternLib.State;

public class GetMessageState extends State<ElevatorContext> {
    public GetMessageState(ElevatorContext context) {
        super(context);
    }

    @Override
    protected void onEntry() {
        context.sendStateUpdate();
    }

    /**
     * Algorithm to select next state, can execute event dependant exit code.
     *
     * @return The next state.
     */
    @Override
    protected State<ElevatorContext> selectNextState() {
        SystemMessage msg = context.receive();

        if (msg instanceof MoveElevatorCommand) {
            context.setDir(((MoveElevatorCommand) msg).direction());
            return new MoveElevatorState(context);
        }
        if (msg instanceof MovePassengersCommand) {
            context.storeNewPassengers(((MovePassengersCommand) msg).newPassengers());
            return new MovePassengersState(context);
        }
        throw new RuntimeException("Message not implemented yet");
    }
}
