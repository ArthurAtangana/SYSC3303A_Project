package Subsystem.ElevatorSubsytem;

import Configuration.Config;
import Configuration.Configurator;
import Messaging.Messages.Commands.MoveElevatorCommand;
import Messaging.Messages.Commands.MovePassengersCommand;
import Messaging.Messages.Direction;
import Messaging.Messages.Events.DestinationEvent;
import Messaging.Messages.Events.ElevatorStateEvent;
import Messaging.Messages.Events.ReceiverBindingEvent;
import Messaging.Transceivers.Receivers.Receiver;
import Messaging.Transceivers.Transmitters.Transmitter;
import Subsystem.Subsystem;
import com.sun.jdi.InvalidTypeException;
import StatePatternLib.*;

import java.util.HashMap;

/**
 * Elevator class which models an elevator in the simulation.
 *
 * @version Iteration-2
 *
 * @author Braeden Kloke
 * @version March 5, 2024
 * Implemented state pattern. Decision to implement states as subclasses of Elevator
 * to allow various state methods to access private methods within Elevator.
 * Alternative would be making some Elevator methods public, but I'm sure this will cause
 * the world to implode. So best not.
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
    }

    /**
     * Elevator travels to a specified floor.
     *
     * @param direction the direction to travel towards.
     */
    private void move(Direction direction) {
        System.out.printf("Elevator %s: Going %s from floor %s.%n",this.elevNum,direction,this.currentFloor);
        try {
            Thread.sleep(this.travelTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        currentFloor += direction.getDisplacement();

        System.out.printf("Elevator %s: Elevator reached floor #%s%n", this.elevNum, this.currentFloor);
    }
    private void unload(){
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
    private void load(MovePassengersCommand command){
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
    private void sendStateUpdate(){
        ElevatorStateEvent stateEvent = new ElevatorStateEvent(elevNum, currentFloor, passengerCountMap);
        transmitterToScheduler.send(stateEvent);
    }

    @Override
    public void run() {
        // Set initial state.
        //
        // Decision to set initial state in method run because the elevator
        // has no state until method run is invoked.
        //
        // Alternative would be setting initial state in constructor but this
        // causes funny business with transmitters and receivers.
        setState(new ReceivingState(this));

        while (true){
            // Wait until an event occurs in this state machine
            event = receiver.dequeueMessage();

            // Handle the event occurring in this state machine
            if (event instanceof MoveElevatorCommand) {
                ((ElevatorState) state).handleMoveElevatorCommand();
            } else if (event instanceof MovePassengersCommand) {
                ((ElevatorState) state).handleMovePassengersCommand();
            } else {
                InvalidTypeException e = new InvalidTypeException("Event type received cannot be handled by this subsystem.");
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Abstract class representing an elevator state.
     *
     * @author Braeden Kloke
     * @version March 5, 2024
     */
    public abstract class ElevatorState extends State {

        public ElevatorState(Context context) {
            super(context);
        }

        /**
         * Handles state machine behaviour when event MoveElevatorCommand occurs.
         *
         * @author Braeden Kloke
         */
        public void handleMoveElevatorCommand() {}

        /**
         * Handles state machine behaviour when event MovePassengersCommand occurs.
         *
         * @author Braeden Kloke
         */
        public void handleMovePassengersCommand() {}
    }

    /**
     * Class representing the state for an elevator receiving system messages.
     *
     * @author Braeden Kloke
     * @version March 5, 2024
     */
    public class ReceivingState extends ElevatorState {

        public ReceivingState(Context context) {
            super(context);
        }

        @Override
        public void entry() {
            Elevator elevator = (Elevator) this.context;
            elevator.sendStateUpdate();
        }

        @Override
        public void handleMoveElevatorCommand() {
            setState(new MovingState(context));
        }

        @Override
        public void handleMovePassengersCommand() {
            setState(new LoadingState(context));
        }
    }

    /**
     * Class representing the state for an elevator moving.
     *
     * @author Braeden Kloke
     * @version March 5, 2024
     */
    public class MovingState extends ElevatorState {
        public MovingState(Context context) {
            super(context);
        }

        @Override
        public void doActivity() {
            ((Elevator) context).move(((MoveElevatorCommand) event).direction());

            // Once done, transition back to receiving state
            setState(new ReceivingState(context));
        }
    }

    /**
     * Class representing the state for an elevator loading / unloading passengers.
     *
     * @author Braeden Kloke
     * @version March 5, 2024
     */
    public class LoadingState extends ElevatorState {

        public LoadingState(Context context) {
            super(context);
        }

        @Override
        public void entry() {
            ((Elevator) context).unload();
        }
        @Override
        public void doActivity() {
            ((Elevator) context).load(((MovePassengersCommand) event));

            // Once done, transition back to receiving state
            setState(new ReceivingState(context));
        }
    }
}
