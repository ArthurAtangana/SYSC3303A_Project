package Messaging.Commands;

import Messaging.Events.DestinationEvent;

/**
 * TODO: Docs
 *
 * @author Alexandre Marques
 * @version Iteration-2
 */
public record PassengerArrivedCommand(int floorNum, DestinationEvent passenger) implements SystemCommand {
    /**
     * Match floor number with the given key.
     *
     * @param key The floor number of the floor trying to process this event.
     * @return True if the floor has the appropriate key, indicating this message is addressed to it.
     */
    @Override
    public boolean matchKey(int key) {
        return key == floorNum;
    }
}
