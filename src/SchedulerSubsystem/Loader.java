package SchedulerSubsystem;

import ElevatorSubsytem.ElevatorUtilities;
import Messaging.Commands.MovePassengersCommand;
import Messaging.Commands.SendPassengersCommand;

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

    public Loader(ElevatorStateEvent event, DMA_Transmitter txFloor, DMA_Transmitter txElevator) {
        elevatorState = event;
        this.txFloor = txFloor;
        this.txElevator = txElevator;

        this.receiver = new DMA_Receiver();
        this.txThis = new DMA_Transmitter(receiver);
    }
    /**
     * Runs this operation.
     */
    @Override
    public void run() {
        // Send command to get passengers from floor
        System.out.println("loader: ask passengers from the floor");
        txFloor.send(new SendPassengersCommand(elevatorState.currentFloor(),
                ElevatorUtilities.getPassengersDirection(elevatorState.passengerCountMap().keySet()), txThis));
        // Receiver passengers
        System.out.println("Loader: receive passengers from the floor");
        ArrayList<DestinationEvent> passengers = ((PassengerLoadEvent) receiver.receive()).passengers();
        // Send passengers to elevator
        System.out.println("Loader: send passengers to the elevator");
        txElevator.send(new MovePassengersCommand(elevatorState.elevatorNum(), passengers));
    }
}
