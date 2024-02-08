/**
 * DestinationEvent record which models an elevator traveling to a destination
 * in service of a passenger request.
 *
 * @version 20240202
 */

package Networking.Events;

import Networking.Direction;
/**
 * DestinationEvent holds all data related to an elevator traveling to a
 * destination.
 * @param destinationFloor Floor that the elevator is going to.
 * @param direction The direction a passenger would like to go (UP/DOWN/STOPPED).
 */
public record DestinationEvent
        (int destinationFloor, Direction direction)
        implements SystemEvent {}
