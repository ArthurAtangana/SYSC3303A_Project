/**
 * Floor class which models a floor in the simulation.
 *
 * @version 20240202
 */

package FloorSubsystem;
import Networking.Events.ElevatorStateEvent;
import Networking.Receivers.DMA_Receiver;

public class Floor implements Runnable {
    private int floorLamp;
    private final DMA_Receiver receiver;

    public Floor(int floorNumber, DMA_Receiver receiver) {
        this.floorLamp = floorNumber;
        this.receiver = receiver;
    }
    /**
     * Setting the lamp to display which floor the elevator is on.
     * @param floorNumber The floor number that the elevator is currently on.
     */
    private void setLamp(int floorNumber) {
        floorLamp = floorNumber;
        System.out.println("The floor sets its lamp display to " + floorLamp);
    }

    /**
     * receives events from the scheduler for the floor to process.
     */
    private void receiveEvent(){
        ElevatorStateEvent elevatorStateEvent = (ElevatorStateEvent) receiver.receive();
        System.out.println("Floor received the information for its lamp from the scheduler.");
        setLamp(elevatorStateEvent.currentFloor());
    }
    @Override
    public void run() {
        while (true) {
            receiveEvent();
        }
    }
}
