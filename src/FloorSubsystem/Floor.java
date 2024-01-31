package FloorSubsystem;
import SchedulerSubsystem.Scheduler;
import SchedulerSubsystem.SchedulerEvent;
import Utlities.Passenger;
import java.util.ArrayList;


public class Floor implements Runnable {
    private ArrayList<Passenger> passengers;
    private int floorLamp;
    private Scheduler scheduler;

    public Floor(int floorNumber, Scheduler scheduler) {
        this.passengers = new ArrayList<>();
        this.floorLamp = floorNumber;
        this.scheduler = scheduler;
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
     * @param schedulerEvent
     */
    public void sendPassengerRequestToScheduler(SchedulerEvent schedulerEvent) {

    }

    /**
     * Sends floor button request to specified elevator.
     * @param schedulerEvent
     */
    public void sendPassengerRequestToElevator(SchedulerEvent schedulerEvent) {
        // Note this requires a reference to the specified elevator??

    }

    @Override
    public void run() {

    }

}
