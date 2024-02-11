/**
 * Elevator class which models an elevator in the simulation.
 *
 * @version 20240202
 */

package ElevatorSubsytem;

import Networking.Direction;
import Networking.Messages.*;
import Networking.Receivers.DMA_Receiver;
import Networking.Transmitters.DMA_Transmitter;
import com.sun.jdi.InvalidTypeException;

import java.util.HashMap;
import java.util.Set;

public class Elevator implements Runnable {
    /** Single floor travel time */
    private static final long TRAVEL_TIME = 8500;
    private  static final long LOAD_TIME = 1000;
    private final int elevNum;
    private int currentFloor;
    @Deprecated
    private Direction direction;
    private final HashMap<DestinationEvent, Integer> passengerCountMap;
    private final DMA_Transmitter transmitterToScheduler;
    private final DMA_Receiver receiver;

    public Elevator(int elevNum, DMA_Receiver receiver, DMA_Transmitter transmitter) {
        this.currentFloor = 0;
        this.elevNum = elevNum;
        this.direction = Direction.STOPPED;
        this.passengerCountMap = new HashMap<>();
        this.transmitterToScheduler = transmitter;
        this.receiver = receiver;
    }

    /**
     * Elevator travels to a specified floor.
     *
     * @param direction
     */
    private void move(Direction direction) {
        if (direction == Direction.UP){
            currentFloor += 1;
        }
        else if (direction == Direction.DOWN){
            currentFloor -= 1;
        }
        System.out.println("Elevator: Going " + direction + ", to floor" + currentFloor + ".");
        try {
            Thread.sleep(TRAVEL_TIME);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Elevator: Elevator is on floor " + this.currentFloor + ".");
    }

    /**
     *
     * @return
     */
    private Direction elevatorDirection(){
        Set<DestinationEvent> destinationEvents = passengerCountMap.keySet();
        // TODO: guard check on every direction being the same.
        Direction direction = null;
        for (DestinationEvent e : destinationEvents){
            if (direction == null){
                direction = e.direction();
            }
            if (direction != e.direction()){
                throw new RuntimeException("Missmatched passenger direction in elevator");
            }
        }
        return direction;
    }
    private void unload(){
        Direction direction = elevatorDirection();
        if (elevatorDirection() == null){
            return;
        }
        try {
            // each passenger takes LOAD_TIME to leave the elevator.
            Thread.sleep(LOAD_TIME * passengerCountMap.remove(new DestinationEvent(currentFloor,direction)));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Loads passengers in and/or out of the elevator
     */
    private void load(MovePassengersCommand command){
        // load passengers into the elevator, taking LOAD_TIME per passengers waiting on the floor.
        try {
            Thread.sleep(LOAD_TIME * command.newPassengers().size());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // for every passenger that board, we add them to the passengerCountMap.
        for (DestinationEvent e : command.newPassengers()){
            passengerCountMap.merge(e,1, Integer::sum);
            // if the key exists in passengerCountMap, increment value by 1. if not, add new entry.
        }
    }
    /**
     * Update scheduler with this elevator's state.
     */
    private void sendStateUpdate(){
        ElevatorStateEvent stateEvent = new ElevatorStateEvent(elevNum, currentFloor, direction, passengerCountMap);
        transmitterToScheduler.send(stateEvent);
    }

    /**
     * Process the given event, identify type and select an action or activity based on it.
     *
     * @param event The event to process
     * @throws InvalidTypeException If it receives an event type this class cannot handle
     */
    private void processMessage(SystemMessage event) throws InvalidTypeException {
        // Note: Cannot switch on type, if we want to refactor selection, look into visitor pattern.\
        if (event instanceof SystemCommand && !((SystemCommand) event).matchKey(this.elevNum)){
            return;
        }
        if (event instanceof MoveElevatorCommand)
            move(((MoveElevatorCommand) event).direction());
        else if (event instanceof MovePassengersCommand) {
            unload();
            load((MovePassengersCommand) event);
        }
        else // Default, should never happen
            throw new InvalidTypeException("Event type received cannot be handled by this subsystem.");
    }

    @Override
    public void run() {
        while (true){
            try {
                processMessage(receiver.receive());
            } catch (InvalidTypeException e) {
                throw new RuntimeException(e);
            }
            sendStateUpdate();
        }
    }
}
