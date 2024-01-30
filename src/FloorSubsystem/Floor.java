package FloorSubsystem;
import SchedulerSubsystem.Scheduler;
import Utlities.Passenger;
import java.util.ArrayList;


public class Floor implements Runnable {
    private ArrayList<Passenger> passengers;
    private int floorLamp;
    private Scheduler scheduler;

    public Floor() {
        this.passengers = new ArrayList<>();
        this.floorLamp = Integer.parseInt(Thread.currentThread().getName());
    }

    /**
     * Receives notification from scheduler that the elevator floor has changed.
     */
    public void receiveElevatorFloorDisplayChangeRequest() {
        // uses setLamp
    }

    /**
     * Setting the lamp to display which floor the elevator is on.
     * @param floorNumber The floor number that the elevator is currently on.
     */
    public void setLamp(int floorNumber) {

    }

    /**
     * Sends floor button request to scheduler.
     * @param passengerEvent
     */
    public void sendPassengerRequestToScheduler(Passenger passengerEvent) {

    }

    /**
     * Sends floor button request to specified elevator.
     * @param schedulerEvent
     */
    public void sendPassengerRequestToElevator(Passenger schedulerEvent) {
        // Note this requires a reference to the specified elevator??

    }

    @Override
    public void run() {


    }

}
