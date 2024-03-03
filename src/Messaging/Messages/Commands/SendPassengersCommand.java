package Messaging.Messages.Commands;

import Messaging.Messages.Direction;
import Messaging.Transceivers.Transmitters.TransmitterDMA;

/**
 * SendPassengersCommand Record, Command to floor to send passengers back through the provided transmitter.
 * Passing a transmitter allows for dynamic creation of receivers on the scheduler end.
 *
 * @param floorNum The floor number the command is addressed to.
 * @param tx The transmitter through which the passengers should be sent.
 *
 * @author Alexandre Marques
 * @version Iteration-2
 */
public record SendPassengersCommand(int floorNum, Direction dir, TransmitterDMA tx) implements SystemCommand {
    /**
     * Match floor number with the given key.
     * @param key The floor number of the floor trying to process this event.
     * @return True if the floor has the appropriate key, indicating this message is addressed to it.
     */
    @Override
    public boolean matchKey(int key) {
        return key == floorNum;
    }
}
