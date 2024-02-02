package ElevatorSubsytem;

import Networking.Direction;
import Networking.Events.DestinationEvent;
import Networking.Events.ElevatorStateEvent;
import Networking.Events.FloorInputEvent;
import Networking.Receivers.DMA_Receiver;
import Networking.Transmitters.DMA_Transmitter;

import java.util.ArrayList;

public class Elevator implements Runnable {
    /** Single floor travel time */
    public final long TRAVEL_TIME = 8500;
    private int currentFloor;
    private Direction direction;
    private ArrayList<FloorInputEvent> floorInputEventList;
    private final DMA_Transmitter transmitterToScheduler;
    private final DMA_Receiver receiver;

    public Elevator(DMA_Receiver receiver, DMA_Transmitter transmitter) {
        this.currentFloor = 0;
        this.direction = Direction.STOPPED;
        this.floorInputEventList = new ArrayList<>();
        this.transmitterToScheduler = transmitter;
        this.receiver = receiver;
    }

    /**
     * processes events received from the scheduler
     */
    public void getScheduling(){
        DestinationEvent destination = (DestinationEvent) receiver.receive();
        System.out.println("going in "+ destination.direction() + " direction.");
        this.direction = destination.direction();
        System.out.println("going to " + destination.destinationFloor());
        travelToFloor(destination.destinationFloor());
    }

    /**
     * Elevator travels to a specified floor.
     * @param floorNumber
     */
    public void travelToFloor(int floorNumber) {
        // Requires use of TRAVEL_TIME
        this.currentFloor = floorNumber;
        System.out.println("elevator is on floor " + this.currentFloor);
        // multiply travel time by num floor
        try {
            Thread.sleep(TRAVEL_TIME);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        ElevatorStateEvent arrivedAtFloorEvent = new ElevatorStateEvent(currentFloor,direction, floorInputEventList);
        transmitterToScheduler.send(arrivedAtFloorEvent);
    }
    @Override
    public void run() {
        while (true){
            getScheduling();
        }
    }

}
