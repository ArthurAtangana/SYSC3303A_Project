/**
 * DMA_Receiver class, provides a way to receive messages from DMA_Transmitters.
 * DMA (Direct Memory Access) name comes from how the transmitters/receivers are implemented:
 * by storing an address to the receivers' buffers and setting messages in them.
 *
 * @author Alexandre Marques
 * @version Iteration-1
 */
package Networking.Receivers;

import Networking.Messages.SystemCommand;
import Networking.Messages.SystemMessage;

import java.util.ArrayList;
import java.util.List;

public class DMA_Receiver implements Receiver{
    // Message buffer
    private final List<SystemMessage> msgBuf;
    private final int key;

    /**
     * Default DMA_Receiver constructor.
     */
    public DMA_Receiver(){
        this(-1); // invalid key, will not match against anything
    }

    /**
     * DMA_Receiver with key to check commands against.
     */
    public DMA_Receiver(int key) {
        this.key = key;
        this.msgBuf = new ArrayList<>();
    }

    /**
     * Receives an event from a Transmitter.
     * Blocks if no event has arrived yet.
     * DMA_Receiver receives by looking at the state of its internal buffer.
     *
     * @return A message sent from a transmitter.
     */
    @Override
    public synchronized SystemMessage receive() {
        while(msgBuf.isEmpty()){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return msgBuf.remove(0);
    }

    /**
     * Asynchronous receive method to update the buffer from the DMA_Transmitter.
     * @param msg The message to store in the message buffer.
     */
    public void setMessage(SystemMessage msg) {
        // GUARD: If the message type is a command, only accept it if it is addressed to this receiver.
        if (msg instanceof SystemCommand && !((SystemCommand) msg).matchKey(this.key))
            return;
        // Store message and notify if waiting.
        synchronized (msgBuf) {
            msgBuf.add(msg);
            notify(); // Single wait type, single notify
        }
    }
}
