package StatePatternLib;

public abstract class Context implements Runnable {
    // FIXME: If there's a way to "late bind" the State generic type, that would be ideal...
    //  In the mean time, the typing in State should suffice to enforce correct State types in Context classes.
    private State curState;
    // TODO in Concrete state: add persistent state info

    public Context() {
        setState(null); // TODO in Concrete State: Replace by start state
    }

    public void setState(State newState) {
        curState = newState;
    }

    @Override
    public void run() {
        curState.start();
    }

    // TODO in Concrete State: Add application logic, package private methods to be called by state activities,
    //  and/or when selecting next state.

    // Note: Can implement MVC by having an "UpdateView" triggered on exit in abstract "State";
    //  Send a message with relevant state packaged for view to process and show.
}
