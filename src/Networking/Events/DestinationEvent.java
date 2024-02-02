package Networking.Events;

import Networking.Direction;

import java.util.ArrayList;

/**
 * Passenger holds all data related to a passenger
 * @param destinationFloor Floor that the elevator is going to.
 * @param direction The direction a passenger would like to go (UP/DOWN/STOPPED).
 */
public record DestinationEvent
        (int destinationFloor, Direction direction)
        implements ElevatorSystemEvent {}
