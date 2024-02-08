/**
 * DMA_Transmitter class, provides a way to send messages to DMA_Receivers.
 * DMA (Direct Memory Access) name comes from how the transmitters/receivers are implemented:
 * by storing an address to the receivers' buffers and setting messages in them.
 *
 * @author Alexandre Marques
 * @version Iteration-1
 */
package Networking.Transmitters;

import Networking.Messages.SystemEvent;
import Networking.Receivers.DMA_Receiver;

import java.util.ArrayList;

public class DMA_Transmitter implements Transmitter {
    // The receivers to send messages to (can be seen as a list of "pointers" to update)
    private final ArrayList<DMA_Receiver> destinationReceivers;

    /**
     * DMA_Transmitter single receiver constructor.
     *
     * @param destReceiver The receiver to send messages to from this transmitter.
     */
    public DMA_Transmitter(DMA_Receiver destReceiver) {
        // Wraps receiver into array to maintain same behavior with many receivers
        destinationReceivers = new ArrayList<>();
        destinationReceivers.add(destReceiver);
    }

    /**
     * DMA_Transmitter list constructor. Supports multiple receiver addresses.
     *
     * @param destReceivers The receivers to send messages to from this transmitter.
     */
    public DMA_Transmitter(ArrayList<DMA_Receiver> destReceivers) {
        destinationReceivers = destReceivers;
    }


    /**
     * Sends a message (event) to receivers.
     * DMA_Transmitter directly sets the message in the buffers of its DMA_Receivers.
     *
     * @param event The message to be sent to receivers.
     */
    @Override
    public void send(SystemEvent event) {
        destinationReceivers.forEach(d -> d.setMessage(event));
    }
}
