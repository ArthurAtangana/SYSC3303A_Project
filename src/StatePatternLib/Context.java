package StatePatternLib;

import Messaging.Messages.SystemMessage;

/**
 * Abstract class representing the context of a state machine.
 *
 * @author Braeden Kloke
 * @version March 6, 2024
 */
public abstract class Context {

    protected State state; // Current state of state machine

    // Event acting on state machine
    //
    // Researched state machine patterns assume there is no attached data
    // from events acting on the state machine. In other words, the fact
    // that an event has happened is all the information the state machine
    // needs to modify its state.
    //
    // However, our state machine design requires the data contained in the event.
    // Thus, I've decided to store the event as a protected attribute.
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
