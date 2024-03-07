package Subsystem.ElevatorSubsytem;

import Configuration.Config;
import Configuration.Configurator;
import Messaging.Messages.Commands.MovePassengersCommand;
import Messaging.Messages.Direction;
import Messaging.Messages.Events.DestinationEvent;
import Messaging.Messages.Events.ElevatorStateEvent;
import Messaging.Messages.Events.ReceiverBindingEvent;
import Messaging.Messages.SystemMessage;
import Messaging.Transceivers.Receivers.Receiver;
import Messaging.Transceivers.Transmitters.Transmitter;
import Subsystem.Subsystem;
import StatePatternLib.*;

import java.util.HashMap;

/**
 * Elevator class which models an elevator in the simulation.
 *
 * @version Iteration-2
 *
 * @author Braeden Kloke
 * @version March 6, 2024
 * Implemented state pattern.
 */
public class Elevator extends Context implements Runnable, Subsystem {
    /** Single floor travel time */
    private final int elevNum;
    private int currentFloor;
    private final long travelTime;
    private final long loadTime;
    private final HashMap<DestinationEvent, Integer> passengerCountMap;
    private final Transmitter<Receiver> transmitterToScheduler;
    private final Receiver receiver;

    public Elevator(int elevNum, Receiver receiver, Transmitter transmitter) {

        // Configure Elevator from JSON
        String jsonFilename = "res/system-config-00.json";
        System.out.println("Configuring Elevator " + elevNum + "...");
        Config config = (new Configurator(jsonFilename).getConfig());
        this.travelTime = config.getTravelTime();
        this.loadTime = config.getLoadTime();

        this.currentFloor = 0;
        this.elevNum = elevNum;
        this.passengerCountMap = new HashMap<>();
        this.transmitterToScheduler = transmitter;
        this.receiver = receiver;

        // Notify scheduler of new subsystem creation -> could fit in subsystem super class
        this.transmitterToScheduler.send(new ReceiverBindingEvent(receiver, this.getClass()));
        setNextState(new ReceivingState(this));
    }

    /**
     * Elevator travels to a specified floor.
     *
     * @param direction the direction to travel towards.
     */
    void move(Direction direction) {
        System.out.printf("Elevator %s: Going %s from floor %s.%n",this.elevNum,direction,this.currentFloor);
        try {
            Thread.sleep(this.travelTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        currentFloor += direction.getDisplacement();

        System.out.printf("Elevator %s: Elevator reached floor #%s%n", this.elevNum, this.currentFloor);
    }
    void unload(){
        Direction direction = ElevatorUtilities.getPassengersDirection(passengerCountMap.keySet());
        if (direction == null){
            return;
        }
        try {
            // each passenger takes loadTime to leave the elevator.
            System.out.println("Passenger unloading from the elevator: " + passengerCountMap.get(new DestinationEvent(currentFloor, direction)));
            Thread.sleep(loadTime * passengerCountMap.remove(new DestinationEvent(currentFloor,direction)));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e) {
            // no passengers to unload, do nothing.
        }
    }
    /**
     * Loads passengers in and/or out of the elevator
     */
    void load(MovePassengersCommand command){
        System.out.println("Loading passengers: " + command.newPassengers());
        // load passengers into the elevator, taking LOAD_TIME per passengers waiting on the floor.
        try {
            Thread.sleep(loadTime * command.newPassengers().size());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // for every passenger that board, we add them to the passengerCountMap.
        for (DestinationEvent e : command.newPassengers()){
            passengerCountMap.merge(e,1, Integer::sum);
            // if the key exists in passengerCountMap, increment value by 1. if not, add new entry.
        }
        System.out.println("Passengers in the elevator: " + passengerCountMap.toString());
    }
    /**
     * Update scheduler with this elevator's state.
     */
    void sendStateUpdate(){
        ElevatorStateEvent stateEvent = new ElevatorStateEvent(elevNum, currentFloor, passengerCountMap);
        transmitterToScheduler.send(stateEvent);
    }
    SystemMessage receive() {
        System.out.println("receive in elevator");
        SystemMessage event = receiver.dequeueMessage();
        System.out.println(event);
        return event;
    }

    @Override
    public void run() {
        while (currentState != null){
            currentState.runState();
        }
    }
}