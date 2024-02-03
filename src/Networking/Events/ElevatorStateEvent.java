package Networking.Events;

import Networking.Direction;

import java.util.ArrayList;

/**
 * ElevatorStateEvent record, holds all data related to the state of an elevator during
 * operation.
 * @param currentFloor Floor that the elevator is at.
 * @param direction The direction a passenger would like to go (UP/DOWN/STOPPED).
 * @param destinationEvents list of floorInputEvents in the elevator.
 *
 * @author Alexandre Marques - 101189743
 * @version Iteration-2
 */
public record ElevatorStateEvent
        (int currentFloor, Direction direction, ArrayList<DestinationEvent> destinationEvents)
        implements SystemEvent {}
