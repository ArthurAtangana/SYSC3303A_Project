package SchedulerSubsystem;

import Messaging.Messages.Commands.MovePassengersCommand;
import Messaging.Messages.Commands.SendPassengersCommand;

import Messaging.Messages.Direction;
import Messaging.Messages.Events.DestinationEvent;
import Messaging.Messages.Events.ElevatorStateEvent;
import Messaging.Messages.Events.PassengerLoadEvent;
import Messaging.Transceivers.Receivers.ReceiverDMA;
import Messaging.Transceivers.Transmitters.TransmitterDMA;

import java.util.ArrayList;


/**
 * Loader class is a runnable that is used to load appropriate passengers into an elevator.
 *
 * The loader sends a SendPassengersCommand to the floor, receives passengers from
 * the floor, and then sends a MovePassengersCommand to the elevator.
 *
 * @author Alexandre Marques
 * @version Iteration-2
 */
public class Loader implements Runnable {
    private final ElevatorStateEvent elevatorState;
    private final TransmitterDMA txElevator;
    private final TransmitterDMA txFloor;
    private final ReceiverDMA receiver;
    private final TransmitterDMA txThis;
    private final Direction elevatorDirection;

    public Loader(ElevatorStateEvent event, TransmitterDMA txFloor, TransmitterDMA txElevator, Direction elevatorDirection) {
        elevatorState = event;
        this.txFloor = txFloor;
        this.txElevator = txElevator;
        this.elevatorDirection = elevatorDirection;

        this.receiver = new ReceiverDMA(0);
        this.txThis = new TransmitterDMA(receiver);
    }
    /**
     * Runs this operation.
     */
    @Override
    public void run() {
        // Send command to get passengers from floor
        txFloor.send(new SendPassengersCommand(elevatorState.currentFloor(), elevatorDirection, txThis));
        // Receiver passengers
        ArrayList<DestinationEvent> passengers = ((PassengerLoadEvent) receiver.receive()).passengers();
        // Send passengers to elevator
        txElevator.send(new MovePassengersCommand(elevatorState.elevatorNum(), passengers));
    }
}
