package Messaging.Messages.Events;

import Messaging.Messages.Direction;

import java.io.Serializable;

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
        (int destinationFloor, Direction direction)
        implements SystemEvent, Serializable {}
