package StatePatternLib;

/**
 * Abstract class representing the state of a state machine.
 *
 * @author Braeden Kloke
 * @version March 4, 2024
 */
public abstract class State {

    protected Context context;

    /**
     * Parametric constructor.
     *
     * @param context Context of state machine that this is a state of.
     *
     * @author Braeden Kloke
     * @version March 4, 2024
     */
    public State(Context context) { this.context = context; }


    /**
     * Action to be done upon entering a state.
     *
     * @author Braeden Kloke
     * @version March 4, 2024
     */
    public void entry() {}
}
