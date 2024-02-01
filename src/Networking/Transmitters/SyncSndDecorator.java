package Networking.Transmitters;

import Networking.Events.ElevatorSystemEvent;
import Networking.Receivers.SyncRcvDecorator;
import com.sun.jdi.InvalidTypeException;

public class SyncSndDecorator extends BaseSndDecorator {
    // FIXME: Find a way to abstract DMA
    SyncSndDecorator(Transmitter transmitter) throws InvalidTypeException {
        super(transmitter);
        // Check for DMA transmitter
        if(!(transmitter instanceof DMA_Transmitter)) {
            throw new InvalidTypeException("Transmitter not using DMA.");
        }
        // Check for Sync receiver
        if (!(((DMA_Transmitter) transmitter).getRcv() instanceof SyncRcvDecorator)){
            throw new InvalidTypeException("Receiver not using Sync decorator.");
        }
    }

    @Override
    public void send(ElevatorSystemEvent event) {
        super.send(event);
        // Notify for sync send TODO: check if this works.
        // TODO: Add a catch for when there are no threads to notify?
        ((DMA_Transmitter) transmitter).getRcv().notify();
    }
}
