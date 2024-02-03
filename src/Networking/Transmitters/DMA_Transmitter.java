package Networking.Transmitters;

import Networking.Events.ElevatorSystemEvent;
import Networking.Receivers.DMA_Receiver;

import java.util.ArrayList;

public class DMA_Transmitter implements Transmitter {
    private final ArrayList<DMA_Receiver> destinationReceivers;

    public DMA_Transmitter(ArrayList<DMA_Receiver> destReceivers) {
        destinationReceivers = destReceivers;
    }

    public DMA_Transmitter(DMA_Receiver destReceiver) {
        destinationReceivers = new ArrayList<>();
        destinationReceivers.add(destReceiver);
    }

    @Override
    public void send(ElevatorSystemEvent event) {
        destinationReceivers.forEach(d -> d.setMessage(event));
    }
}
