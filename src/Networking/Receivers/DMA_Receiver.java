/**
 * DMA_Receiver class, provides a way to receive messages from DMA_Transmitters.
 * DMA (Direct Memory Access) name comes from how the transmitters/receivers are implemented:
 * by storing an address to the receivers' buffers and setting messages in them.
 *
 * @author Alexandre Marques
 * @version Iteration-1
 */
package Networking.Receivers;

import Networking.Events.ElevatorSystemEvent;

import java.util.ArrayList;

public class DMA_Receiver implements Receiver{
    // Message buffer
    private final ArrayList<ElevatorSystemEvent> msgBuf;

    /**
     * Default DMA_Receiver constructor.
     */
    public DMA_Receiver(){
        msgBuf = new ArrayList<>();
    }

    /**
     * Receives an event from a Transmitter.
     * Blocks if no event has arrived yet.
     * DMA_Receiver receives by looking at the state of its internal buffer.
     *
     * @return A message sent from a transmitter.
     */
    @Override
    public synchronized ElevatorSystemEvent receive() {
        while(msgBuf.isEmpty()){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return msgBuf.remove(0);
    }

    // DMA Receiver asynch receiving method
    /**
     * Asynchronous receive method to update the buffer from the DMA_Transmitter.
     * @param msg The message to store in the message buffer.
     */
    public synchronized void setMessage(ElevatorSystemEvent msg){
        msgBuf.add(msg);
        notifyAll();
    }
}
