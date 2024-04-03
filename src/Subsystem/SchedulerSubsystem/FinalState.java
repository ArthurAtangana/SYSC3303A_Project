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
        ((Scheduler)context).displaySimulationStatistics();
        System.exit(0);
    }
}
