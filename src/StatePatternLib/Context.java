package StatePatternLib;

import Messaging.Messages.SystemMessage;

/**
 * Abstract class representing the context of a state machine.
 *
 * @author Braeden Kloke
 * @version March 4, 2024
 */
public abstract class Context {

    protected State state; // Current state of state machine
    protected SystemMessage event; // Event occurring in state machine

    /**
     * Default constructor for state machine.
     *
     * Newly constructed state machine requires initial state to be set
     * using method setState.
     *
     * @author Braeden Kloke
     * @version March 4, 2024
     */
    public Context() {
        this.state = null;
        this.event = null;
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
        this.state.entry();
        this.state.doActivity();
    }
}
