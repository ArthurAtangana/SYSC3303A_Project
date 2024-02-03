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
    private final int floorNum;
    private final DMA_Receiver receiver;

    public Floor(int floorNumber, DMA_Receiver receiver) {
        this.floorNum = floorNumber;
        this.receiver = receiver;
        // Start elevator location at 0 until an update is received
        floorLamp = 0;
    }
    /**
     * Setting the lamp to display which floor the elevator is on.
     * @param floorNumber The floor number that the elevator is currently on.
     */
    private void setLamp(int floorNumber) {
        floorLamp = floorNumber;
        System.out.println("Floor #"+floorNum+": Lamp display updated to floor#"+ floorLamp + ".");
    }

    /**
     * receives events from the scheduler for the floor to process.
     */
    private void receiveEvent(){
        ElevatorStateEvent elevatorStateEvent = (ElevatorStateEvent) receiver.receive();
        setLamp(elevatorStateEvent.currentFloor());
    }
    @Override
    public void run() {
        while (true) {
            receiveEvent();
        }
    }
}
