package Messaging.Messages.Events;

import java.util.ArrayList;

/**
 * PassengerLoadEvent record, holds a list of passengers to load onto an elevator.
 * Intended flow: Floor -> Scheduler(Loader).
 *
 * @param passengers The passengers to be loaded on the elevator
 * @author Alexandre Marques
 * @version Iteration-2
 */
public record PassengerLoadEvent(int elevNumber,
                                 ArrayList<DestinationEvent> passengers) implements SystemEvent {
}