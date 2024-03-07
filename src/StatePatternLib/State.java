package StatePatternLib;

/**
 * Abstract class representing the state of a state machine.
 *
 * @author Braeden Kloke
 * @version March 5, 2024
 */
public abstract class State {

    protected Context context;

    /**
     * Parametric constructor.
     *
     * @param context Context of state machine that this is a state of.
     *
     * @author Braeden Kloke
     */
    public State(Context context) { this.context = context; }


    /**
     * Action to be done upon entering a state.
     *
     * @author Braeden Kloke
     */
    public void entry() {}

    /**
     * Activity to be done while in a state.
     *
     * @author Braeden Kloke
     */
    public void doActivity() {
        // Would have liked to name method 'do'. But 'do' is a reserved keyword in Java.
    }

    /**
     * Action to be done upon exiting a state.
     *
     * @author Braeden Kloke
     */
    public void exit() {

    }
}
