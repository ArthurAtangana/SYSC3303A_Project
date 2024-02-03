/**
 * Elevator class which models an elevator in the simulation.
 *
 * @version 20240202
 */

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
     * Processes events received from the scheduler.
     */
    private void getScheduling(){
        DestinationEvent destination = (DestinationEvent) receiver.receive();
        System.out.println("Elevator: Going in "+ destination.direction() + " direction.");
        this.direction = destination.direction();
        System.out.println("Elevator: Going to floor " + destination.destinationFloor() + ".");
        travelToFloor(destination.destinationFloor());
    }

    /**
     * Elevator travels to a specified floor.
     * @param floorNumber
     */
    private void travelToFloor(int floorNumber) {
        this.currentFloor = floorNumber;
        System.out.println("Elevator: Elevator is on floor " + this.currentFloor + ".");
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
