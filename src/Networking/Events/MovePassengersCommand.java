package Networking.Events;

import java.util.ArrayList;

/**
 * MovePassengersCommand Record, Command to elevator to load passengers onto the elevator (if applicable),
 * and offload passengers in the elevator (if applicable).
 * The choice is a logical OR: at least one of the two occurs, possibly both depending on the overall state of the system.
 *
 * @param elevNum The elevator the command is addressed to.
 * @param newPassengers The passengers to load into the elevator (CAN BE NULL)
 *
 * @author Alexandre Marques - 101189743
 * @version Iteration-2
 */
public record MovePassengersCommand(int elevNum, ArrayList<DestinationEvent> newPassengers) implements SystemCommand {
    /**
     * Match elevator number with the given key.
     * @param key The elevator number of the elevator trying to process this event.
     * @return True if the elevator has the appropriate key, indicating this message is addressed to it.
     */
    @Override
    public boolean matchKey(int key) {
        return key == elevNum;
    }
}
