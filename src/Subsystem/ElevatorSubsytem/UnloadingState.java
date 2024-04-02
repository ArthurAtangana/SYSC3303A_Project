package Subsystem.ElevatorSubsytem;

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
        context.setNextState(new ReceivingState(context));
    }
}
