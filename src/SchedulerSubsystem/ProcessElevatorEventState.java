package SchedulerSubsystem;

import StatePatternLib.State;
import Subsystem.SchedulerSubsystem.StateMachine.SchedulerContext;
import Subsystem.SubsystemContext;

public class ProcessElevatorEventState extends State<SchedulerContext> {
    protected ProcessElevatorEventState(SchedulerContext context) {
        super(context);
    }

    @Override
    protected State<SchedulerContext> selectNextState() {
        return null;
    }
}
