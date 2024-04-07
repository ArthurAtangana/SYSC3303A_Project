package Subsystem.ElevatorSubsytem;

import Messaging.Messages.Commands.MovePassengersCommand;
import StatePatternLib.Context;
import StatePatternLib.State;

/**
 * Class representing the state for an elevator loading / unloading passengers.
 *
 * @author Braeden Kloke
 * @version March 6, 2024
 */
public class UnloadingState extends State {

    public UnloadingState(Context context) {
        super(context);
    }

    @Override
    public void entry() {
        String msg = "UnloadingState:Entry";
        ((Elevator) context).logger.log(Logging.Logger.LEVEL.DEBUG, ((Elevator) context).logId, msg);

    }

    @Override
    public void doActivity() {
        ((Elevator) context).unload();
    }

    @Override
    public void exit() {
        // After unload, a load command always comes in.
        // Avoid requesting a command from scheduler by entering receiving state again
        context.setNextState(new LoadingState(context, (MovePassengersCommand) ((Elevator) context).receive()));
    }
}
