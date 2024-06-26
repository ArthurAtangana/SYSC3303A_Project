package StatePatternLib;

/**
 * Abstract class representing the context of a state machine.
 *
 * @author Braeden Kloke
 * @version March 6, 2024
 */
public abstract class Context implements Runnable{

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
    @Override
    public void run() {
        while (currentState != null){
            currentState.runState();
        }
    }
}