package Networking.Events;

import Networking.Direction;

import java.util.ArrayList;

/**
 * FloorInputEvent holds all data related to a passenger
 * @param currentFloor Floor that the elevator is at.
 * @param direction The direction a passenger would like to go (UP/DOWN/STOPPED).
 * @param floorInputEvents list of floorInputEvents in the elevator.
 */
public record ElevatorStateEvent
        (int currentFloor, Direction direction, ArrayList<FloorInputEvent> floorInputEvents)
        implements ElevatorSystemEvent {}
