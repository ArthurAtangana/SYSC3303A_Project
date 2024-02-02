package Networking.Transmitters;

import Networking.Events.ElevatorSystemEvent;
import Networking.Receivers.DMA_Receiver;

public class DMA_Transmitter implements Transmitter {
    private DMA_Receiver destinationReceiver;

    public DMA_Transmitter() {}
    public void setDestinationReceiver(DMA_Receiver destinationReceiver) {
        if (this.destinationReceiver != null) {
            return;
        }
        this.destinationReceiver = destinationReceiver;
    }

    @Override
    public void send(ElevatorSystemEvent event) {
        destinationReceiver.setMessage(event);
    }
}
