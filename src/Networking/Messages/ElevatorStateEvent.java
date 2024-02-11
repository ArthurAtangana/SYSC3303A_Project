package Networking.Messages;

import Networking.Direction;

import java.util.HashMap;

/**
 * ElevatorStateEvent record, holds all data related to the state of an elevator during
 * operation.
 *
 * @param elevatorNum The elevator number identifying the elevator
 * @param currentFloor Floor that the elevator is at.
 * @param direction The direction a passenger would like to go (UP/DOWN/STOPPED).
 *                  DEPRECATED: Likely to remove if not needed by end of iteration 2 (passengerCountMap can replace)
 * @param passengerCountMap Map passengers (Destination Events) to the count of identical passengers in the elevator.
 *                          Can derive destination set, and total passenger count of the elevator from this.
 *
 * @author Alexandre Marques
 * @version Iteration-2
 */
public record ElevatorStateEvent
        (int elevatorNum, int currentFloor, @Deprecated Direction direction,
         HashMap<DestinationEvent, Integer> passengerCountMap)
        implements SystemEvent {}
