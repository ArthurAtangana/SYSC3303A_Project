package Subsystem.FloorSubsystem.StateMachine;

import Messaging.Direction;
import Messaging.Events.DestinationEvent;
import Messaging.Events.PassengerLoadEvent;
import Messaging.Receivers.DMA_Receiver;
import Messaging.Transmitters.DMA_Transmitter;
import Messaging.Transmitters.Transmitter;
import Subsystem.SchedulerSubsystem.SchedulerContext;
import Subsystem.SubsystemContext;

import java.util.ArrayList;

public class FloorContext extends SubsystemContext {
    // Unique key generator
    private static int key_count = 0;
    private final DMA_Transmitter txScheduler; // Can transmitters be static? -> Minor performance decrease?
    // Application state
    private int lampNumber;
    private final ArrayList<DestinationEvent> floorPassengers;
    private Transmitter tempLoaderTx;
    private Direction loadDir;

    public FloorContext() {
        super(key_count++);
        floorPassengers = new ArrayList<>();
        txScheduler = new DMA_Transmitter();
        lampNumber = 0;
        tempLoaderTx = null;
        loadDir = null;
    }

    public void bindToScheduler(SchedulerContext sched) {
        // TODO: Replace when UDP receivers are in
        txScheduler.addRx((DMA_Receiver) sched.getRx());
        // Scheduler call back
        sched.bindToFloor((DMA_Receiver) rx);
    }

    void setLamp(int floorNum) {
        lampNumber = floorNum;
    }

    void storePassenger(DestinationEvent passenger) {
        floorPassengers.add(passenger);
    }

    void setLoaderTx(Transmitter tx) {
        tempLoaderTx = tx;
    }

    void setLoadDir(Direction dir) {
        loadDir = dir;
    }

    void sendPassengers() {
        ArrayList<DestinationEvent> passengersToLoad = new ArrayList<>();
        Direction currentDirection = loadDir;
        // Send passengers with current direction
        for (DestinationEvent dest : floorPassengers) {
            if (dest.direction() == currentDirection) {
                passengersToLoad.add(dest);
            }
        }
        tempLoaderTx.send(new PassengerLoadEvent(passengersToLoad));
        floorPassengers.removeAll(passengersToLoad);
    }
}
