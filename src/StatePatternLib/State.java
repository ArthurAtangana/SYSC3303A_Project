package StatePatternLib;


public abstract class State<ContextT extends Context> {
    protected final ContextT context;

    public State(ContextT context) {
        this.context = context;
    }

    /**
     * Method called on state entry to "start" the state execution.
     * Includes all execution parts of a state, including:
     * 1. OnEntry
     * 2. doActivity
     * 3. newStateSelection (based on events during doActivity)
     * 4. OnExit
     * 5. updating context to new state
     */
    public void start() {
        onEntry();
        doActivity(); // The state has no memory, only the context
        State<ContextT> newState = selectNextState();
        onExit();
        context.setState(newState); // Set next state
        // Start next state if not null (null == end state)
        if (newState != null)
            context.run();
    }

    /**
     * Code which is triggered on state entry.
     */
    protected void onEntry() {
    }


    /**
     * Code which is triggered on state exit.
     */
    protected void onExit() {
    }


    /**
     * Code which is executed until state change.
     * <p>
     * doActivity needs an exit condition by either:
     * - Implementing an event detection mechanism inside.
     * - Having a defined start/exit point independent of external inputs.
     */
    protected void doActivity() {
    }

    /**
     * Algorithm to select next state, can execute event dependant exit code.
     *
     * @return The next state.
     */
    protected abstract State<ContextT> selectNextState();
}
