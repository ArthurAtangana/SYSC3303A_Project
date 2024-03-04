package Messaging.Transceivers.Transmitters;

import Messaging.Messages.SystemMessage;
import Messaging.Transceivers.Receivers.Receiver;

import java.util.ArrayList;

/**
 * Transmitter Interface, defines methods required to transmit Events to receivers.
 *
 * @author Alexandre Marques
 * @version Iteration-1
 */
public abstract class Transmitter<R extends Receiver> {
    // The receivers to send messages to (can be seen as a list of "pointers" to update)
    protected final ArrayList<R> destinationReceivers;

    protected Transmitter() {
        this.destinationReceivers = new ArrayList<>();
    }

    /**
     * Sends a message (event) to receivers.
     * @param event The message to be sent to receivers.
     */
    public abstract void send(SystemMessage event);

    /**
     * Provides a way to "late-bind" receivers to transmitters,
     * as they may be created after the transmitter is created or
     * the reference may be difficult to access on construction.
     *
     * @param destReceiver The receiver to bind to this transmitter.
     */
    public void addReceiver(R destReceiver) {
        destinationReceivers.add(destReceiver);
    }
}
