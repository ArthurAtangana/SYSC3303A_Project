package SchedulerSubsystem;

import Messaging.Events.ElevatorStateEvent;
import Messaging.Events.FloorRequestEvent;
import Messaging.SystemMessage;
import StatePatternLib.State;
import com.sun.jdi.InvalidTypeException;

public class ReceivingState extends State<Scheduler> {
    public ReceivingState(Scheduler context) {
        super(context);
    }

    @Override
    protected State<Scheduler> selectNextState() {
        System.out.println("selectNextState in Receiving State");
        while (true){
            try {
                SystemMessage event = context.receive();
                if (event instanceof ElevatorStateEvent){
                    context.processElevatorEvent((ElevatorStateEvent) event);
                }
                else if (event instanceof FloorRequestEvent){
                    context.storeFloorRequest((FloorRequestEvent) event);
                }
                else // Default, should never happen
                    throw new InvalidTypeException("Event type received cannot be handled by this subsystem.");
            } catch (InvalidTypeException e){
                throw new RuntimeException(e);
            }

        }
    }
}
