package Subsystem.ElevatorSubsytem;

import Messaging.Messages.Commands.MovePassengersCommand;
import StatePatternLib.Context;

/**
 * Class representing the state for an elevator loading / unloading passengers.
 *
 * @author Braeden Kloke
 * @version March 6, 2024
 */
public class LoadingState extends ElevatorState {

    public LoadingState(Context context) {
        super(context);
    }

    @Override
    public void entry() {
        ((Elevator) context).unload();
    }
    @Override
    public void doActivity() {
        ((Elevator) context).load(((MovePassengersCommand) context.event));

        // Once done, transition back to receiving state
        context.setState(new ReceivingState(context));
    }
}
