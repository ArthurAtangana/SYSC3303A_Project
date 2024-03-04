package SchedulerSubsystem;

// TODO: Remove this class -> do some kind of co-operative concurrency...
//  Should be able to send an event, give up control, and resume operation when answer comes in.
//  Priority setting should help prevent starvation and preserve quick service.

/**
 * Loader class is a runnable that is used to load appropriate passengers into an elevator.
 *
 * The loader sends a SendPassengersCommand to the floor, receives passengers from
 * the floor, and then sends a MovePassengersCommand to the elevator.
 *
 * @author Alexandre Marques
 * @version Iteration-2
 */
//@Deprecated
//public class Loader implements Runnable {
//    private final ElevatorStateEvent elevatorState;
//    private final Transmitter<Receiver> txElevator;
//    private final Transmitter<Receiver> txFloor;
//    private final Receiver receiver;
//    private final Transmitter<Receiver> txThis;
//    private final Direction elevatorDirection;
//
//    public Loader(ElevatorStateEvent event, Transmitter<Receiver> txFloor, Transmitter<Receiver> txElevator,
//                  Direction elevatorDirection) {
//        elevatorState = event;
//        this.txFloor = txFloor;
//        this.txElevator = txElevator;
//        this.elevatorDirection = elevatorDirection;
//
//        receiver = new ReceiverDMA(0);
//        // Create transmitter with reference to itself, to pass on to floor later
//        txThis = new TransmitterDMA();
//        txThis.addReceiver(receiver);
//    }
//    /**
//     * Runs this operation.
//     */
//    @Override
//    public void run() {
//        // Send command to get passengers from floor
//        txFloor.send(new SendPassengersCommand(elevatorState.currentFloor(), elevatorDirection, txThis));
//        // Receiver passengers
//        ArrayList<DestinationEvent> passengers = ((PassengerLoadEvent) receiver.dequeueMessage()).passengers();
//        // Send passengers to elevator
//        txElevator.send(new MovePassengersCommand(elevatorState.elevatorNum(), passengers));
//    }
//}
