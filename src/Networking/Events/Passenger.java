package Networking.Events;

import Networking.Direction;
import Networking.Events.ElevatorSystemEvent;

/**
 * Passenger holds all data related to a passenger
 * @param arrivalTime Time the passenger arrives at the floor.
 * @param sourceFloor Floor that the passenger requests an elevator from.
 * @param direction The direction a passenger would like to go (UP/DOWN).
 * @param destinationFloor Floor that the passenger would like to arrive at.
 */
public record Passenger
        (long arrivalTime, int sourceFloor, Direction direction, int destinationFloor)
        implements ElevatorSystemEvent {}
