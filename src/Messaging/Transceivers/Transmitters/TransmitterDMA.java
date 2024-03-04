package Messaging.Transceivers.Transmitters;

import Messaging.Messages.SystemMessage;
import Messaging.Transceivers.Receivers.ReceiverDMA;

/**
 * DMA_Transmitter class, provides a way to send messages to DMA_Receivers.
 * DMA (Direct Memory Access) name comes from how the transmitters/receivers are implemented:
 * by storing an address to the receivers' buffers and setting messages in them.
 *
 * @author Alexandre Marques
 * @version Iteration-1
 */
public class TransmitterDMA extends Transmitter<ReceiverDMA> {
    /**
     * DMA_Transmitter constructor.
     */
    public TransmitterDMA() {
        super();
    }

    /**
     * Sends a message (event) to receivers.
     * DMA_Transmitter directly sets the message in the buffers of its DMA_Receivers.
     *
     * @param event The message to be sent to receivers.
     */
    @Override
    public void send(SystemMessage event) {
        receivers.forEach(d -> d.receiveDMA(event));
    }
}
