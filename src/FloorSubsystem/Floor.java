package FloorSubsystem;
import Networking.Events.ElevatorSystemEvent;
import Networking.Events.FloorButtonPressedEvent;
import Networking.Receivers.DMA_Receiver;
import Networking.Transmitters.DMA_Transmitter;
import Networking.Events.Passenger;
import java.util.ArrayList;


public class Floor implements Runnable {
    private ArrayList<Passenger> passengers;
    private int floorLamp;
    private DMA_Transmitter transmitterToScheduler;

    public Floor(int floorNumber, DMA_Receiver schedulerReceiver) {
        this.passengers = new ArrayList<>();
        this.floorLamp = floorNumber;
        this.transmitterToScheduler = new DMA_Transmitter();
        transmitterToScheduler.setDestinationReceiver(schedulerReceiver);
    }

    /**
     * Receives notification from scheduler that the elevator floor has changed.
     */
    public void receiveElevatorFloorDisplayChangeRequest() {
        // uses setLamp
    }

    /**
     * Setting the lamp to display which floor the elevator is on.
     * @param floorNumber The floor number that the elevator is currently on.
     */
    public void setLamp(int floorNumber) {

    }

    /**
     * Sends floor button request to scheduler.
     * @param systemEvent
     */
    public void sendPassengerRequestToScheduler(ElevatorSystemEvent systemEvent) {
        this.transmitterToScheduler.send(systemEvent);
    }

    /**
     * Checks to see if a passenger has arrived at the floor.
     *
     * @return True if passenger has arrived, false otherwise.
     *
     * @author Braeden & Victoria
     * @version 02/01/2024
     */
    private boolean passengerHasArrived() {
        // Stubbed method
        // TODO if simulation time > arrival time of first passenger in passengers return true
        return true;
    }

    @Override
    public void run() {
        while (true) {
            // see if simulation time > arrival time
            if (passengerHasArrived()) {
                Passenger p = passengers.remove(0);
                FloorButtonPressedEvent e = new FloorButtonPressedEvent(p);
                sendPassengerRequestToScheduler(e);
            }

        }

    }

}
