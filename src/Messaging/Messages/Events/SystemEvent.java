package Messaging.Messages.Events;

import Messaging.Messages.SystemMessage;

/**
 * Interface definition for all events passed in the system to inherit.
 * Events are defined as messages used to hold/pass simple information passed to an ENTIRE subsystem
 * (all instances of the model). Simple actions usually get triggered (store info in fields, show views).
 *
 * @author Alexandre Marques
 * @version Iteration-2
 */
public interface SystemEvent extends SystemMessage { }
