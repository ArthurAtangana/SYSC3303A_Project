package Messaging.Messages.Events;

import Messaging.Messages.Direction;
import Messaging.Messages.Fault;

/**
 * EndSchedulingEvent record, models a kill event to the system. 
 *
 * @param msg A cute message.
 *
 * @author MD
 * @version Iteration-5
 */
public record FloorInputEvent
        (String msg) implements SystemEvent {}
