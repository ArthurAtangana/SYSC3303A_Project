package Messaging.Messages.Events;

/**
 * ClearFloorRequestEvent record, tells the scheduler the floor has been fully serviced after a load.
 * Intended flow: Floor -> Scheduler.
 *
 * @param destinationEvent The destination event referring to the source floor that is sent to the scheduler.
 * @author Alexandre Marques
 * @version Iteration-2
 */
public record ClearFloorRequestEvent
        (DestinationEvent destinationEvent)
        implements SystemEvent {
}
