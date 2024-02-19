package Subsystem.FloorSubsystem;

import Messaging.Receivers.DMA_Receiver;
import Messaging.Transmitters.DMA_Transmitter;
import Subsystem.SchedulerSubsystem.SchedulerContext;
import Subsystem.SubsystemContext;

public class FloorContext extends SubsystemContext {
    // Unique key generator
    private static int key_count = 0;
    private final DMA_Transmitter txScheduler; // Can these be static?

    protected FloorContext(int key) {
        super(key_count++);
        txScheduler = new DMA_Transmitter();
    }

    public void bindToScheduler(SchedulerContext sched) {
        // TODO: Replace when UDP receivers are in
        txScheduler.addRx((DMA_Receiver) sched.getRx());
        // Scheduler call back
        sched.bindToFloor((DMA_Receiver) rx);
    }
}
