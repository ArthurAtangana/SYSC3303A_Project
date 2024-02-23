package SchedulerSubsystem;

import Messaging.Commands.MovePassengersCommand;
import Messaging.Commands.SendPassengersCommand;

import Messaging.Direction;
import Messaging.Events.DestinationEvent;
import Messaging.Events.ElevatorStateEvent;
import Messaging.Events.PassengerLoadEvent;
import Messaging.Receivers.DMA_Receiver;
import Messaging.Transmitters.DMA_Transmitter;

import java.util.ArrayList;


public class Loader implements Runnable {
    private final ElevatorStateEvent elevatorState;
    private final DMA_Transmitter txElevator;
    private final DMA_Transmitter txFloor;
    private final DMA_Receiver receiver;
    private final DMA_Transmitter txThis;
    private final Direction elevatorDirection;

    public Loader(ElevatorStateEvent event, DMA_Transmitter txFloor, DMA_Transmitter txElevator, Direction elevatorDirection) {
        elevatorState = event;
        this.txFloor = txFloor;
        this.txElevator = txElevator;
        this.elevatorDirection = elevatorDirection;

        this.receiver = new DMA_Receiver();
        this.txThis = new DMA_Transmitter(receiver);
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
