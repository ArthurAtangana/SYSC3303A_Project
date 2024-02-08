package SchedulerSubsystem;

import Networking.Messages.*;
import Networking.Receivers.DMA_Receiver;
import Networking.Transmitters.DMA_Transmitter;

import java.util.ArrayList;

public class ElevatorLoader implements Runnable {
    private final ElevatorStateEvent elevatorState;
    private final DMA_Transmitter txElevator;
    private final DMA_Transmitter txFloor;
    private final DMA_Receiver receiver;
    private final DMA_Transmitter txThis;

    public ElevatorLoader(ElevatorStateEvent event, DMA_Transmitter txFloor, DMA_Transmitter txElevator) {
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
        txFloor.send(new SendPassengersCommand(elevatorState.currentFloor(), elevatorState.direction(), txThis));
        // Receiver passengers
        ArrayList<DestinationEvent> passengers = ((PassengerLoadEvent) receiver.receive()).passengers();
        // Send passengers to elevator
        txElevator.send(new MovePassengersCommand(elevatorState.elevatorNum(), passengers));
    }
}
