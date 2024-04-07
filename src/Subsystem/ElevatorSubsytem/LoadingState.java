package Subsystem.ElevatorSubsytem;

import Messaging.Messages.Commands.MovePassengersCommand;
import StatePatternLib.Context;
import StatePatternLib.State;
import Subsystem.Logging.Logger;

/**
 * Class representing the state for an elevator loading / unloading passengers.
 *
 * @author Braeden Kloke
 * @version March 6, 2024
 */
public class LoadingState extends State {

    private final MovePassengersCommand loadCommand;
    public LoadingState(Context context,MovePassengersCommand loadCommand) {
        super(context);
        this.loadCommand = loadCommand;
    }

    @Override
    public void entry() {
        String msg = "LoadingState:Entry";
        ((Elevator)context).logger.log(Logger.LEVEL.DEBUG, ((Elevator)context).logId, msg);
    }
    @Override
    public void doActivity() {
        ((Elevator) context).load(loadCommand);
    }

    @Override
    public void exit() {
        context.setNextState(new ReceivingState(context));
    }
}