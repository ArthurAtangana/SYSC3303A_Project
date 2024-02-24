/**
 * FloorRequestEvent record, holds a destination event and the time the system
 * received the request.
 * Intended flow: Dispatcher -> Scheduler.
 *
 * @param destinationEvent The destination event that is sent to the scheduler.
 * @param time The time that the destination event was requested at.
 *
 * @author Alexandre Marques
 * @version Iteration-2
 */
package Messaging.Events;

public record FloorRequestEvent
        (DestinationEvent destinationEvent, long time)
        implements SystemEvent {
}
