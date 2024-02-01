package SchedulerSubsystem;
import ElevatorSubsytem.Elevator;
import Networking.Events.ElevatorSystemEvent;

import java.util.ArrayList;

public class Scheduler implements Runnable {
    private ArrayList<ElevatorSystemEvent> eventQueue;
    private ArrayList<Thread> elevators;
    private ArrayList<Thread> floors;

    public Scheduler() {
    }

    public void setFloors(ArrayList<Thread> floors) {
        this.floors = floors;
    }

    public void setElevators(ArrayList<Thread> elevators) {
        this.elevators = elevators;
    }

    /**
     * Receives passenger event from floor and creates scheduler event
     * @param systemEvent
     * @return
     */
    public ElevatorSystemEvent receivePassengerRequest(ElevatorSystemEvent systemEvent) {
        return null;
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
