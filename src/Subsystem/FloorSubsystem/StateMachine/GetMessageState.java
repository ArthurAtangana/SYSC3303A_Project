package Subsystem.FloorSubsystem.StateMachine;

import Messaging.Commands.SendPassengersCommand;
import Messaging.Events.DestinationEvent;
import Messaging.Events.ElevatorStateEvent;
import Messaging.SystemMessage;
import StatePatternLib.State;

public class GetMessageState extends State<FloorContext> {
    public GetMessageState(FloorContext context) {
        super(context);
    }

    /**
     * Algorithm to select next state, can execute event dependant exit code.
     *
     * @return The next state.
     */
    @Override
    protected State<FloorContext> selectNextState() {
        SystemMessage msg = context.receive();

        if (msg instanceof ElevatorStateEvent) {
            context.setLamp(((ElevatorStateEvent) msg).currentFloor());
            return new GetMessageState(context);
        } else if (msg instanceof DestinationEvent) {
            context.storePassenger((DestinationEvent) msg);
            return new GetMessageState(context);
        } else if (msg instanceof SendPassengersCommand sndCommand) {
            context.setLoaderTx(sndCommand.tx());
            context.setLoadDir(sndCommand.dir());
            return new SendPassengerState(context);
        }
        throw new RuntimeException("Message not implemented yet");
    }
}
