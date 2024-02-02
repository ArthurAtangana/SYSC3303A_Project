package ElevatorSubsytem;

import Networking.Direction;
import Networking.Events.DestinationEvent;
import Networking.Events.ElevatorStateEvent;
import Networking.Events.Passenger;
import Networking.Receivers.DMA_Receiver;
import Networking.Transmitters.DMA_Transmitter;
import SchedulerSubsystem.Scheduler;

import java.util.ArrayList;

public class Elevator implements Runnable {
    /** Single floor travel time */
    public final long TRAVEL_TIME = 8500;
    private int currentFloor;
    private Direction direction;
    private ArrayList<Passenger> passengerList;
    private Scheduler scheduler;
    private DMA_Transmitter elevatorTransmitter;
    private DMA_Receiver elevatorReceiver;

    public Elevator(Scheduler scheduler) {
        this.currentFloor = 0;
        this.scheduler = scheduler;
        this.direction = Direction.STOPPED;
        this.passengerList = new ArrayList<>();
        this.elevatorTransmitter = new DMA_Transmitter();
        this.elevatorReceiver = new DMA_Receiver();
    }
    public void setSchedulerReceiver(DMA_Receiver schedulerReceiver){
        elevatorTransmitter.setDestinationReceiver(schedulerReceiver);
    }
    public DMA_Receiver getElevatorReceiver(){
        return this.elevatorReceiver;
    }

    /**
     * Returns the boarding or deboarding time given the numberOfPassengers
     *
     * @param numberOfPassengers Amount of passengers boarding/deboarding the elevator.
     * @return Time in seconds
     */
    private double getBoardingTime(int numberOfPassengers) {
        return 1.84 * numberOfPassengers + 6.79;
    }
    public void getScheduling(){
        DestinationEvent destination = (DestinationEvent) elevatorReceiver.receive();
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
        ElevatorStateEvent arrivedAtFloorEvent = new ElevatorStateEvent(currentFloor,direction,passengerList);
        elevatorTransmitter.send(arrivedAtFloorEvent);
    }
    @Override
    public void run() {
        while (true){
            getScheduling();
        }
    }

}
