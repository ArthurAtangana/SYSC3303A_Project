package Subsystem.FloorSubsystem;

import Configuration.Config;
import Messaging.Messages.Commands.PassengerArrivedCommand;
import Messaging.Messages.Commands.SendPassengersCommand;
import Messaging.Messages.Direction;
import Messaging.Messages.Events.*;
import Messaging.Messages.SystemMessage;
import Messaging.Transceivers.Receivers.Receiver;
import Messaging.Transceivers.Receivers.ReceiverComposite;
import Messaging.Transceivers.Receivers.ReceiverDMA;
import Messaging.Transceivers.Transmitters.Transmitter;
import Messaging.Transceivers.Transmitters.TransmitterDMA;
import Subsystem.Logging.Logger;
import Subsystem.Subsystem;
import com.sun.jdi.InvalidTypeException;

import java.util.ArrayList;

/**
 * Floor class which models a floor in the simulation.
 *
 * @version Iteration-5
 */
public class Floor implements Runnable, Subsystem {
    private static final TransmitterDMA allFloorsDMATransmitter = new TransmitterDMA();

    private int floorLamp;
    private final int floorNum;
    private boolean upLamp;
    private boolean downLamp;

    final Config config;
    final Logger logger;
    String logId;

    // Receivers
    private final ReceiverComposite receiverComposite; // Groups multiple receivers into 1 queue

    // Transmitter
    private final Transmitter<? extends Receiver> transmitterToScheduler;

    private final ArrayList<DestinationEvent> passengers;

    private final int topFloor;

    public Floor(Config config, int floorNumber, Receiver receiver, Transmitter<? extends Receiver> transmitterToScheduler) {
        this.floorNum = floorNumber;
        this.transmitterToScheduler = transmitterToScheduler;
        this.config = config;
        this.upLamp = false;
        this.downLamp = false;

        // Logging
        logId = "FLOOR " + this.floorNum;
        logger = new Logger(config.getVerbosity());

        // Start elevator location at 0 until an update is received
        floorLamp = 0;
        passengers = new ArrayList<>();

        // Init receivers
        ReceiverDMA inputEventReceiver = new ReceiverDMA(floorNumber);
        receiverComposite = new ReceiverComposite(floorNumber);
        // Start threads on receivers to claim their queues in the composite
        receiverComposite.claimReceiver(inputEventReceiver);
        receiverComposite.claimReceiver(receiver);

        allFloorsDMATransmitter.addReceiver(inputEventReceiver);

        // Notify scheduler of new subsystem creation -> could fit in subsystem super class
        this.transmitterToScheduler.send(new ReceiverBindingEvent(receiver, this.getClass()));

        // Store highest floor
        topFloor = config.getNumFloors();
    }

    public static TransmitterDMA getFloorsTransmitter() {
        return allFloorsDMATransmitter;
    }

    /**
     * Setting the lamp to display which floor the elevator is on.
     *
     * @param floorNumber The floor number that the elevator is currently on.
     */
    private void setLamp(int floorNumber) {
        floorLamp = floorNumber;
        System.out.println("Floor #"+floorNum+": Lamp display updated to floor#"+ floorLamp + ".");
    }

    /**
     * Illuminate an UP/DOWN button lamp on this floor, if not already lit.
     *
     * @param requestDirection [UP | DOWN] button the passenger has pushed on
     * this floor.
     *
     * @author MD
     * @version Iteration-5
     */
    private void illuminateButtonLamp(Direction requestDirection) {
        
        // Indicator to see if we have lit a lamp.
        boolean illuminated = false;
        // Message storage for log.
        String msg;
        Logger.LEVEL level = Logger.LEVEL.DEBUG;
        
        // Case: Passenger has pushed UP button
        if (requestDirection == Direction.UP) {
            // Case: UP lamp not yet lit. Light it.
            if (!upLamp) {
                upLamp = true;
                illuminated = true;
            }
        }
        // Case: Passenger has pushed DOWN button
        else {
            // Case: DOWN lamp not yet lit. Light it.
            if (!downLamp) {
                downLamp = true;
                illuminated = true;
            }
        }

        // Case: We have lit a lamp. Make it so on console. (INFO)
        if (illuminated) {
            level = Logger.LEVEL.INFO;
            msg = "Illuminating " + requestDirection + " button lamp.";
        }
        // Case: We have NOT lit a lamp. Indicate it is already pushed. (DEBUG)
        else {
            msg = requestDirection + " button lamp is already lit.";
        }

        // Log it.
        logger.log(level, logId, msg);

    }

    /**
     * Extinguish an UP/DOWN button lamp on this floor once an elevator has serviced
     * this request, if not already extinguished.
     *
     * @param servicedDirection [UP | DOWN] the direction the elevator has serviced.
     *
     * @author MD
     * @version Iteration-5
     */
    private void extinguishButtonLamp(Direction servicedDirection) {
        
        // Indicator to see if we have extinguished a lamp.
        boolean extinguished = false;
        // Message storage for log.
        String msg;
        Logger.LEVEL level = Logger.LEVEL.DEBUG;
        
        // Case: Elevator has serviced UP request.
        if (servicedDirection == Direction.UP) {
            // Case: UP lamp lit. Extinguish it.
            if (upLamp) {
                upLamp = false;
                extinguished = true;
            }
        }
        // Case: Elevator has serviced DOWN request.
        else {
            // Case: DOWN lamp not yet lit. Light it.
            if (downLamp) {
                downLamp = false;
                extinguished = true;
            }
        }

        // Case: We have extinguished a lamp. Make it so on console. (INFO)
        if (extinguished) {
            level = Logger.LEVEL.INFO;
            msg = "Extinguishing " + servicedDirection + " button lamp.";
        }
        // Case: We have NOT extinguished a lamp. Indicate it is already out. (DEBUG)
        // NB: This should prolly never happen...
        else {
            msg = servicedDirection + " button lamp is already extinguished.";
        }

        // Log it.
        logger.log(level, logId, msg);

    }


    /**
     * Storing the floor request made by a passenger.
     *
     * @param cmd passenger cmd received from the scheduler.
     */
    private void storePassenger(PassengerArrivedCommand cmd) {
        passengers.add(cmd.passenger());
        // Log
        String msg = "Passenger has requested to go " + cmd.passenger().direction() + " to Floor " + cmd.passenger().destinationFloor() + ".";
        logger.log(Logger.LEVEL.INFO, logId, msg);
        // Light appropriate button lamp, if not already lit.
        illuminateButtonLamp(cmd.passenger().direction());
    }

    /**
     * Sending passengers onto the elevator. Once loaded, they are no longer waiting for
     * service from the floor.
     *
     * @param sendPassengersCommand Passenger load event received from the scheduler.
     */
    // TODO: Check if this can be unsynced? LABELED NOT CRITICAL
    private synchronized void sendPassengers(SendPassengersCommand sendPassengersCommand) {
        ArrayList<DestinationEvent> passengersToLoad = new ArrayList<>();
        Direction currentDirection = sendPassengersCommand.dir();
        String msg;

        // Log: Indicate elevator arrival at floor. 
        msg = "*Ding!* Elevator " + sendPassengersCommand.elevNum() + " has arrived for service.";
        logger.log(Logger.LEVEL.INFO, logId, msg);
        // Extinguish the button lamp for current direction, since elevator has arrived.
        extinguishButtonLamp(currentDirection);

        // Send passengers with current direction
        int curCapacity = sendPassengersCommand.capacity();
        int passengerIndex = 0;
        while (curCapacity > 0 && passengerIndex < passengers.size()) {
            DestinationEvent passenger = passengers.get(passengerIndex);
            // Send passenger if destination floor is in range and requested direction matches elevator direction.
            if (passenger.direction() == currentDirection
                    && passenger.destinationFloor() >= 0
                    && passenger.destinationFloor() <= topFloor) {
                passengersToLoad.add(passenger);
                // Increment our count for prints
                // Log: Indicate a discrete passenger has entered elevator.
                msg = "Passenger is queueing to enter Elevator " + sendPassengersCommand.elevNum() + " to go " + passenger.direction() + " to Floor " + passenger.destinationFloor() + ".";
                logger.log(Logger.LEVEL.INFO, logId, msg);
                // Remove 1 capacity because passenger added elevator
                curCapacity--;
            }
            passengerIndex++;
        }

        transmitterToScheduler.send(new PassengerLoadEvent(sendPassengersCommand.elevNum(), passengersToLoad));
        passengers.removeAll(passengersToLoad);

        // Check if we need to renotify the scheduler that passengers are still waiting at this location
        if (passengers.stream().anyMatch((DestinationEvent d) -> d.direction() == currentDirection)) {
            transmitterToScheduler.send(new FloorRequestEvent(
                    new DestinationEvent(floorNum, currentDirection, null),
                    0)); // Time doesn't matter: it's not being dispatched by dispatcher
        }
    }

    /**
     * Process the given event, identify type and select an action or activity based on it.
     *
     * @param event The event to process
     * @throws InvalidTypeException If it receives an event type this class cannot handle
     */
    private void processMessage(SystemMessage event) throws InvalidTypeException {
        // Note: Cannot switch on type, if we want to refactor selection, look into visitor pattern.
        if (event instanceof ElevatorStateEvent)
            setLamp(((ElevatorStateEvent) event).currentFloor());
        else if (event instanceof SendPassengersCommand) {
            sendPassengers((SendPassengersCommand) event);
        } else if (event instanceof PassengerArrivedCommand) {
            storePassenger((PassengerArrivedCommand) event);
        }
        else // Default, should never happen
            throw new InvalidTypeException("Event type received cannot be handled by this subsystem.");
    }
    @Override
    public void run() {
        while (true) {
            // receiver.receive = receive state. ProcessEvent "selects" the action
            try {
                processMessage(receiverComposite.dequeueMessage());
            } catch (InvalidTypeException e) {
                throw new RuntimeException(e);
            }
        }
    }
}