package Networking.Messages;

import java.util.ArrayList;

/**
 * PassengerLoadEvent record, holds a list of passengers to load onto an elevator.
 * operation.
 *
 * @param passengers The passengers to be loaded on the elevator
 * @author Alexandre Marques
 * @version Iteration-2
 */
public record PassengerLoadEvent(ArrayList<DestinationEvent> passengers) implements SystemEvent {
}
