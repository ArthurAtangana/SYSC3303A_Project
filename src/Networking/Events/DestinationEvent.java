package Networking.Events;

import Networking.Direction;
/**
 * DestinationEvent record, holds data modelling a destination.
 * Floor requests, and passengers in the system can both be modeled as a destination to be served.
 *
 * @param destinationFloor Floor that the elevator is going to.
 * @param direction The direction a passenger would like to go (UP/DOWN/STOPPED).
 *
 * @version iteration-1
 * @author Alexandre Marques
 */
public record DestinationEvent
        (int destinationFloor, Direction direction)
        implements ElevatorSystemEvent {}
