package Messaging.Messages.Events;

/**
 * EndSchedulingEvent record, models a kill event to the system. 
 *
 * @param msg A cute message.
 *
 * @author MD
 * @version Iteration-5
 */
public record EndSchedulingEvent(String msg) implements SystemEvent {}
