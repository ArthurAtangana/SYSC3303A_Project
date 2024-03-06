package StatePatternLib;

import Messaging.Messages.SystemMessage;

/**
 * Abstract class representing the context of a state machine.
 *
 * @author Braeden Kloke
 * @version March 5, 2024
 */
public abstract class Context {

    protected State state; // Current state of state machine

    // Event occurring in state machine
    //
    // Decision to store event as a protected attribute because various
    // event handling methods require data contained in the event.
    //
    // Alternative solution would be to pass event to every method but
    // this seems egregious.
    public SystemMessage event; // TODO: tmp public access modifier

    /**
     * Default constructor for state machine.
     *
     * Newly constructed state machine requires initial state to be set
     * using method setState.
     *
     * @author Braeden Kloke
     */
    public Context() {
        this.state = null;
        this.event = null;
    }

    /**
     * Transitions this state machine from its current state to the
     * given state.
     *
     * @param state State to be transitioned to.
     *
     * @author Braeden Kloke
     */
    public void changeState(State state) {
        // this.state.exit()
        this.state = state;
        this.state.entry();
        this.state.doActivity();
    }
}
