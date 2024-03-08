package StatePatternLib;

import Messaging.Messages.SystemMessage;
import Messaging.Transceivers.Receivers.Receiver;

/**
 * Abstract class representing the context of a state machine.
 *
 * @author Braeden Kloke
 * @version March 6, 2024
 */
public abstract class Context {

    protected State currentState; // Current state of state machine
    /**
     * Default constructor for state machine.
     *
     * @author Braeden Kloke
     */
    public Context() {
    }
    public void setNextState(State nextState){
        currentState = nextState;
    }
}
