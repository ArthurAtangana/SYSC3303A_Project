package Subsystem.ElevatorSubsytem;

import Configuration.Config;
import Logging.Logger;
import Configuration.Configurator;
import Messaging.Messages.Commands.MovePassengersCommand;
import Messaging.Messages.Direction;
import Messaging.Messages.Events.DestinationEvent;
import Messaging.Messages.Events.ElevatorStateEvent;
import Messaging.Messages.Events.ReceiverBindingEvent;
import Messaging.Messages.SystemMessage;
import Messaging.Transceivers.Receivers.Receiver;
import Messaging.Transceivers.Transmitters.Transmitter;
import StatePatternLib.Context;
import Subsystem.Subsystem;

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
public class Elevator extends Context implements Subsystem {
    /** Single floor travel time */
    private final int elevNum;
    private int currentFloor;
    private final long travelTime;
    private final long loadTime;
    private final HashMap<DestinationEvent, Integer> passengerCountMap;
    private final Transmitter<?> transmitterToScheduler;
    private final Receiver receiver;
    final Logging.Logger logger;
    String logId;

    public Elevator(Config config, int elevNum, Receiver receiver, Transmitter<?> transmitter) {

        this.travelTime = config.getTravelTime();
        this.loadTime = config.getLoadTime();

        this.currentFloor = 0;
        this.elevNum = elevNum;
        this.passengerCountMap = new HashMap<>();
        this.transmitterToScheduler = transmitter;
        this.receiver = receiver;

        // Logging
        logId = "ELEVATOR " + this.elevNum;
        logger = new Logging.Logger(config.getVerbosity());

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
        // Log
        String msg = "Going " + direction + " from Floor " + this.currentFloor + ".";
        logger.log(Logger.LEVEL.INFO, logId, msg);

        try {
            Thread.sleep(this.travelTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        currentFloor += direction.getDisplacement();

        // Log
        msg = "Reached floor " + this.currentFloor + ".";
        logger.log(Logger.LEVEL.INFO, logId, msg);
    }
    void unload(){
        Direction direction = ElevatorUtilities.getPassengersDirection(passengerCountMap.keySet());
        if (direction == null){
            return;
        }
        try {
            // Log
            String msg = "Unloading Passenger: " + passengerCountMap.get(new DestinationEvent(currentFloor, direction));
            logger.log(Logger.LEVEL.INFO, logId, msg);
            // each passenger takes loadTime to leave the elevator.
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
        // Log
        String msg = "Loading Passengers: " + command.newPassengers();
        logger.log(Logger.LEVEL.INFO, logId, msg);
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
        // Log
        msg = "Passengers in the elevator: " + passengerCountMap.toString();
        logger.log(Logger.LEVEL.DEBUG, logId, msg);
    }
    /**
     * Update scheduler with this elevator's state.
     */
    void sendStateUpdate(){
        ElevatorStateEvent stateEvent = new ElevatorStateEvent(elevNum, currentFloor, passengerCountMap);
        transmitterToScheduler.send(stateEvent);
    }
    SystemMessage receive() {
        SystemMessage event = receiver.dequeueMessage();
        // Log
        String msg = "Received SystemMessage: " + event;
        logger.log(Logger.LEVEL.DEBUG, logId, msg);
        return event;
    }
}
