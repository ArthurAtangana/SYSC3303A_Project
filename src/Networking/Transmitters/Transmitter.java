/**
 * Transmitter Interface, defines methods required to transmit Events to receivers.
 *
 * @author Alexandre Marques
 * @version Iteration-1
 */

package Networking.Transmitters;

import Networking.Events.ElevatorSystemEvent;

public interface Transmitter {
    /**
     * Sends a message (event) to receivers.
     * @param event The message to be sent to receivers.
     */
    void send(ElevatorSystemEvent event);
}
