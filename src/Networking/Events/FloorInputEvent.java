/**
 * FloorInputEvent record which models an input event to the system. These events
 * may be supplied to the system via an input file, which is then parsed to
 * generate the event.
 *
 * @version 20240202
 */

package Networking.Events;

import Networking.Direction;

/**
 * FloorInputEvent holds all data related to a passenger
 * @param arrivalTime Time the passenger arrives at the floor.
 * @param sourceFloor Floor that the passenger requests an elevator from.
 * @param direction The direction a passenger would like to go (UP/DOWN).
 * @param destinationFloor Floor that the passenger would like to arrive at.
 */
public record FloorInputEvent
        (long time, int sourceFloor, Direction direction, int destinationFloor)
        implements SystemEvent {}
