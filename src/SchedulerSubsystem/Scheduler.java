package SchedulerSubsystem;
import Networking.Messages.*;
import Networking.Receivers.DMA_Receiver;
import Networking.Transmitters.DMA_Transmitter;

public class Scheduler implements Runnable {
    private final DMA_Transmitter transmitterToFloor;
    private final DMA_Transmitter transmitterToElevator;
    private final DMA_Receiver receiver;

    public Scheduler(DMA_Receiver receiver, DMA_Transmitter transmitterToFloor, DMA_Transmitter transmitterToElevator) {
        this.receiver = receiver;
        this.transmitterToElevator = transmitterToElevator;
        this.transmitterToFloor = transmitterToFloor;
    }

    /**
     * receives SystemEvent from the floor subsystem or elevator subsystem.
     */
    private void receiveEvent(){
        SystemEvent systemEvent = receiver.receive();
        if (systemEvent instanceof ElevatorStateEvent){
            processElevatorEvent((ElevatorStateEvent) systemEvent);
        } else if (systemEvent instanceof DestinationEvent) {
            processDestinationEvent((DestinationEvent) systemEvent);
        }
    }
    /**
     * process event received from the floor (up or down request)
     * and sends it to the elevator.
     * @param destinationEvent a destination event
     */
    private void processDestinationEvent(DestinationEvent destinationEvent) {
        transmitterToElevator.send(destinationEvent);
    }
    /**
     *  process elevator event with elevator state (current floor, direction, passengerList)
     *  and sends it to the floor.
     * @param elevatorStateEvent an elevator state event
     */
    private void processElevatorEvent(ElevatorStateEvent elevatorStateEvent) {
        transmitterToFloor.send(elevatorStateEvent);
    }
    @Override
    public void run() {
        while (true){
            receiveEvent();
        }
    }
}
