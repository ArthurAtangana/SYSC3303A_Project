package ElevatorSubsytem;

import Networking.SystemEvent;
import SchedulerSubsystem.Scheduler;

public class Elevator implements Runnable {
    /** Single floor travel time */
    public final double TRAVEL_TIME = 8.5;
    private int currentFloor;
    private Scheduler scheduler;

    public Elevator(Scheduler scheduler) {
        this.currentFloor = 0;
        this.scheduler = scheduler;
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

    /**
     * Elevator travels to a specified floor.
     * @param floorNumber
     */
    public void travelToFloor(int floorNumber) {
        // Requires use of TRAVEL_TIME
    }

    /**
     * Elevator tells the scheduler that it has arrived at a floor
     *
     * @param schedulerEvent
     */
    public void sendArrivedAtFloorMessage(SystemEvent schedulerEvent){

    }
    @Override
    public void run() {

    }

}
