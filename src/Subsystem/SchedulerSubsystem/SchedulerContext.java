package Subsystem.SchedulerSubsystem;

import Messaging.Receivers.DMA_Receiver;
import Messaging.Transmitters.DMA_Transmitter;
import Subsystem.SubsystemContext;

public class SchedulerContext extends SubsystemContext {
    // Unique key generator
    private static int key_count = 0;
    // Transmitters
    private final DMA_Transmitter txFloor;
    private final DMA_Transmitter txElevator;

    protected SchedulerContext() {
        super(++key_count);
        txFloor = new DMA_Transmitter();
        txElevator = new DMA_Transmitter();
    }

    public void bindToFloor(DMA_Receiver rx) {
        txFloor.addRx(rx);
    }

    public void bindToElevator(DMA_Receiver rx) {
        txElevator.addRx(rx);
    }
}
