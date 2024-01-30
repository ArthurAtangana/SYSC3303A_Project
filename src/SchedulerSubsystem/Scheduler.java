package SchedulerSubsystem;
import ElevatorSubsytem.Elevator;
import Utlities.Passenger;

import java.util.ArrayList;

public class Scheduler implements Runnable {
    private ArrayList<SchedulerEvent> eventQueue;
    private ArrayList<Thread> elevators;
    private ArrayList<Thread> floors;

    public Scheduler(ArrayList<Thread> elevators, ArrayList<Thread> floors) {
        this.elevators = elevators;
        this.floors = floors;
    }

    /**
     * Receives passenger event from floor and creates scheduler event
     * @param passengerEvent
     * @return
     */
    public SchedulerEvent receivePassengerRequest(Passenger passengerEvent) {
        return null;
    }

    /**
     * Uses scheduler event to further process request
     * @param schedulerEvent
     */
    private void processAndSchedulePassengerRequest(SchedulerEvent schedulerEvent) {

    }

    /**
     * Informs elevator to go to a specified floor number.
     * @param floorNumber
     */
    public void goToFloor(int floorNumber, Elevator elevator){

    }

    @Override
    public void run() {

    }


}
