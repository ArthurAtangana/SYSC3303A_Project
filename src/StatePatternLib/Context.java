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
    protected SystemMessage event;

    /**
     * Default constructor for state machine.
     *
     * Newly constructed state machine requires initial state to be set
     * using method setState.
     *
     * @author Braeden Kloke
     */
    public Context() {
        state = null;
        event = null;
    }

    /**
     * Transitions this state machine from its current state to the
     * given state.
     *
     * @param nextState State to be transitioned to.
     *
     * @author Braeden Kloke
     */
    public void changeState(State nextState) {
        if (state != null) {state.exit();}
        state = nextState;
        state.entry();
        state.doActivity();
    }

    /**
     * Retrieves the event that has most recently acted on this state machine.
     *
     * @return Event that has most recently acted on this state machine.
     *
     * @author Braeden Kloke
     */
    public SystemMessage getEvent() {return event;}
}
