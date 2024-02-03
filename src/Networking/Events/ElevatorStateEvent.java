package Networking.Events;

import Networking.Direction;

import java.util.ArrayList;

/**
 * ElevatorStateEvent record, holds all data related to the state of an elevator during
 * operation.
 * @param currentFloor Floor that the elevator is at.
 * @param direction The direction a passenger would like to go (UP/DOWN/STOPPED).
 * @param DestinationEvent list of DestinationEvents in the elevator (think of these as elevator passengers).
 *
 * @version iteration-1
 */
public record ElevatorStateEvent
        (int currentFloor, Direction direction, ArrayList<DestinationEvent> DestinationEvent)
        implements ElevatorSystemEvent {}
