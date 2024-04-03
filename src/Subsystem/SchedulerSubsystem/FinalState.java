package Subsystem.SchedulerSubsystem;

import Logging.Logger;
import StatePatternLib.Context;
import StatePatternLib.State;

/**
 * Scheduler FSM State: Final State.
 *
 * Class which models the final state for the Scheduler FSM.
 *
 * Responsibilities:
 * - Display simulation statistics to console.
 *
 * @author Braeden Kloke
 * @version Iteration-5
 */
public class FinalState extends State {

    /**
     * Parametric constructor.
     *
     * @param context Context of state machine that this is a state of.
     */
    public FinalState(Context context) {
        super(context);
    }

    @Override
    public void entry() {
        String msg = "Displaying simulation statistics to console.";
        ((Scheduler)context).logger.log(Logger.LEVEL.INFO, ((Scheduler) context).logId, msg);
        System.exit(0);
    }
}
