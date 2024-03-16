package Messaging.Messages.Events;

import java.io.Serializable;

/**
 * FloorRequestEvent record, holds a destination event referring to the source floor
 * and the time the floor received the request.
 * Intended flow: Dispatcher -> Scheduler.
 *
 * @param destinationEvent The destination event referring to the source floor that is sent to the scheduler.
 * @param time The time that the destination event was requested at.
 *
 * @author Alexandre Marques
 * @version Iteration-2
 */
public record FloorRequestEvent
        (DestinationEvent destinationEvent, long time)
        implements SystemEvent {
}
