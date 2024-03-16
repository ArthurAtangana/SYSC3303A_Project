package Messaging.Transceivers.Receivers;

import Messaging.Messages.SystemMessage;


/**
 * DMA_Receiver class, provides a way to receive messages from DMA_Transmitters.
 * DMA (Direct Memory Access) name comes from how the transmitters/receivers are implemented:
 * by storing an address to the receivers' buffers and setting messages in them.
 *
 * @author Alexandre Marques
 * @version Iteration-1
 */
public class ReceiverDMA extends Receiver implements SerializableReceiver{
    /**
     * DMA_Receiver with key to check commands against.
     */
    public ReceiverDMA(int key) {
        super(key);
    }

    /**
     * Returns serializable version of this receiver.
     * For DMA, returns itself (already serializable).
     * @return The serializable equivalent to this receiver instance.
     */
    @Override
    public SerializableReceiver getSerializableReceiver() {
        return this;
    }

    /**
     * Asynchronous receive method to update the buffer from the DMA_Transmitter.
     * Will discard messages not addressed to this receiver (command type messages)
     *
     * @param msg The message to store in the message buffer.
     */
    public void receiveDMA(SystemMessage msg) {
        enqueueMessage(msg);
    }
}
