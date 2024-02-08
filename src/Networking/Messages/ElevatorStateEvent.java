package Networking.Messages;

import Networking.Direction;

import java.util.ArrayList;

/**
 * ElevatorStateEvent record, holds all data related to the state of an elevator during
 * operation.
 *
 * @param elevatorNum The elevator number identifying the elevator
 * @param currentFloor Floor that the elevator is at.
 * @param direction The direction a passenger would like to go (UP/DOWN/STOPPED).
 * @param destinationEvents list of floorInputEvents in the elevator.
 *
 * @author Alexandre Marques
 * @version Iteration-2
 */
public record ElevatorStateEvent
        (int elevatorNum, int currentFloor, Direction direction, ArrayList<DestinationEvent> destinationEvents)
        implements SystemEvent {}
