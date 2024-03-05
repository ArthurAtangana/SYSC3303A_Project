package StatePatternLib;

/**
 * Abstract class representing the context of a state machine.
 *
 * @author Braeden Kloke
 * @version March 4, 2024
 */
public abstract class Context {

    State state; // Current state of state machine

    /**
     * Parametric constructor for state machine.
     *
     * @param initialState Initial state of state machine.
     *
     * @author Braeden Kloke
     * @version March 4, 2024
     */
    public Context(State initialState) {
        setState(initialState);
    }

    /**
     * Transitions the state machine to the given state.
     *
     * @param state State to be transitioned to.
     *
     * @author Braeden Kloke
     * @version March 4, 2024
     */
    public void setState(State state) {
        this.state = state;
    }
}
