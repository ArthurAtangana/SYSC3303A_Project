package Messaging.Commands;

import Messaging.Direction;

import java.io.Serializable;

/**
 * MoveElevatorCommand Record, Command to elevator to move until the next floor is reached.
 *
 * @param elevNum The elevator the command is addressed to.
 * @param direction The direction of travel for the elevator
 *
 * @author Alexandre Marques
 * @version Iteration-2
 */
public record MoveElevatorCommand(int elevNum, Direction direction) implements SystemCommand, Serializable {
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
