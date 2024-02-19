package Messaging.Events;


/**
 * TODO: docs
 *
 * @author Alexandre Marques
 * @version iteration-2
 */
public record FloorRequestEvent
        (DestinationEvent destinationEvent, long time)
        implements SystemEvent {
}
