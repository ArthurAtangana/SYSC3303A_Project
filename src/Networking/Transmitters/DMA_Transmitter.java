package Networking.Transmitters;

import Networking.Events.ElevatorSystemEvent;
import Networking.Receivers.BlockingReceiver;
import Networking.Receivers.DMA_Receiver;
import Networking.Receivers.Receiver;

public class DMA_Transmitter implements Transmitter {
    final private DMA_Receiver destinationReceiver;

    public DMA_Transmitter(DMA_Receiver destRcv) {
        this.destinationReceiver = destRcv;
    }

    @Override
    public void send(ElevatorSystemEvent event) {
        destinationReceiver.setMessage(event);
        destinationReceiver.notifyMsgReceived();
    }
}
