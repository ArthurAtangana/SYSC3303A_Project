package SchedulerSubsystem;

import StatePatternLib.State;
import Subsystem.SchedulerSubsystem.StateMachine.SchedulerContext;

public class StoringFloorRequestState extends State<SchedulerContext> {
    public StoringFloorRequestState(SchedulerContext context) {
        super(context);
    }

    @Override
    protected State<SchedulerContext> selectNextState() {
        return null;
    }
}
