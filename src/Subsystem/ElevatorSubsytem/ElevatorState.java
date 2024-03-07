package Subsystem.ElevatorSubsytem;

import StatePatternLib.*;

/**
 * Abstract class representing an elevator state.
 *
 * @author Braeden Kloke
 * @version March 6, 2024
 */
public abstract class ElevatorState extends State {

    public ElevatorState(Context context) {
        super(context);
    }

    /**
     * Handles state machine behaviour when event MoveElevatorCommand occurs.
     *
     * @author Braeden Kloke
     */
    public void handleMoveElevatorCommand() {}

    /**
     * Handles state machine behaviour when event MovePassengersCommand occurs.
     *
     * @author Braeden Kloke
     */
    public void handleMovePassengersCommand() {}
}
