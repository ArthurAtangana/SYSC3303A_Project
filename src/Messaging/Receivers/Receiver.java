/**
 * Receiver Interface, defines methods required to receive Events from transmitters.
 *
 * @author Alexandre Marques
 * @version Iteration-1
 */
package Messaging.Receivers;

import Messaging.SystemMessage;

public interface Receiver {
    /**
     * Receives an event from a Transmitter.
     * Blocks if no event has arrived yet.
     *
     * @return A message sent from a transmitter.
     */
    SystemMessage receive();
}
