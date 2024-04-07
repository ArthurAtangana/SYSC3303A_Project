package Messaging.Messages.Events;

import Messaging.Messages.Direction;
import Messaging.Messages.Fault;

/**
 * DestinationEvent record, holds data modelling a destination.
 * Floor requests, and passengers in the system can both be modeled as a destination to be served.
 *
 * @param destinationFloor Floor that the elevator is going to.
 * @param direction The direction a passenger would like to go (UP/DOWN/STOPPED).
 *
 * @version Iteration-1
 * @author Alexandre Marques
 */
public record DestinationEvent
        (int destinationFloor, Direction direction, Fault faultType)
        implements SystemEvent {

    /**
     * Override default equals method to accept a null faultType as a wildcard match against other faults
     *
     * @param o the reference object with which to compare.
     * @return True if all fields match,
     * or all fields except fault match and one of the two objects has a null (wildcard) fault.
     */
    @Override
    public boolean equals(Object o) {
        // Check identical
        if (this == o)
            return true;
        // Type cast o to this type
        if (!(o instanceof DestinationEvent de))
            return false;
        // Check normal fields (no wildcards)
        if (de.destinationFloor != this.destinationFloor || de.direction != this.direction)
            return false;
        // Wildcard fault type exists (no need to check it)
        if (de.faultType == null || this.faultType == null)
            return true;
        // Check fault type
        return de.faultType == this.faultType;
    }

    /**
     * Override hashCode such that faults are all grouped the same for equality.
     *
     * @return HashCode of record.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        // Combine hash codes of each field
        result = prime * result + destinationFloor;
        result = prime * result + ((direction == null) ? 0 : direction.hashCode());
        // Do not do faults, They should all be grouped together. Loses efficiency but allows equality with nulls

        return result;
    }
}