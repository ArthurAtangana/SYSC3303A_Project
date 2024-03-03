package Messaging.Commands;

import Messaging.Events.DestinationEvent;

import java.io.Serializable;

/**
 * PassengerArrivedCommand Record, Command to floor that a passenger has arrived at the floor
 * with an intended destination.
 *
 * @param floorNum The floor number the command is addressed to.
 * @param passenger The destination the passenger would like to go to.
 *
 * @author Alexandre Marques
 * @version Iteration-2
 */
public record PassengerArrivedCommand(int floorNum, DestinationEvent passenger) implements SystemCommand, Serializable {
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
