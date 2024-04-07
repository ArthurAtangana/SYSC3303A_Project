package Messaging.Transceivers.Transmitters;

import Messaging.Messages.SystemMessage;
import Messaging.Transceivers.Receivers.Receiver;
import Messaging.Transceivers.Receivers.SerializableReceiver;

import java.util.ArrayList;

/**
 * Transmitter Interface, defines methods required to transmit Events to receivers.
 *
 * @author Alexandre Marques
 * @version Iteration-1
 */
public abstract class Transmitter<R extends Receiver> {
    // The receivers to send messages to (can be seen as a list of "pointers" to update)
    protected final ArrayList<R> receivers;
    private final Class<R> receiverType;

    protected Transmitter(Class<R> receiverType) {
        this.receiverType = receiverType;
        this.receivers = new ArrayList<>();
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
     * Will ignore receivers that don't match this transmitter type.
     *
     * @param receiver The receiver to bind to this transmitter.
     */
    public void addReceiver(SerializableReceiver receiver) {
        // Add IFF the receiver matches the expected type
        if (receiverType != null && receiverType.isInstance(receiver)) {
            // Cast to the specific subclass.
            @SuppressWarnings("unchecked") // JVM warns due to type erasure, but we do type checking above
            R specificReceiver = (R) receiver;
            receivers.add(specificReceiver);
        }
    }
}