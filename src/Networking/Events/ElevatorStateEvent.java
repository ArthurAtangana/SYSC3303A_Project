package Networking.Events;

import Networking.Direction;

import java.util.ArrayList;

/**
 * Passenger holds all data related to a passenger
 * @param currentFloor Floor that the elevator is at.
 * @param direction The direction a passenger would like to go (UP/DOWN/STOPPED).
 * @param passengers list of passengers in the elevator.
 */
public record ElevatorStateEvent
        (int currentFloor, Direction direction, ArrayList<Passenger> passengers)
        implements ElevatorSystemEvent {}
