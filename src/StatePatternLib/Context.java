package StatePatternLib;

import Messaging.Messages.SystemMessage;
import Messaging.Transceivers.Receivers.Receiver;

/**
 * Abstract class representing the context of a state machine.
 *
 * @author Braeden Kloke
 * @version March 6, 2024
 */
public abstract class Context implements Runnable{

    protected State currentState; // Current state of state machine
    protected Receiver receiver;
    /**
     * Default constructor for state machine.
     *
     * @author Braeden Kloke
     */
    public Context(Receiver receiver) {
        this.receiver = receiver;
    }
    public void setNextState(State nextState){
        currentState = nextState;
    }
    public SystemMessage receive(){
        return receiver.dequeueMessage();
    }
    @Override
    public void run() {
        while (currentState != null){
            currentState.runState();
        }
    }
}
