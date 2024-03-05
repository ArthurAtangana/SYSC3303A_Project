package Messaging.Messages.Events;

import Messaging.Transceivers.Receivers.Receiver;
import Subsystem.Subsystem;

public record ReceiverBindingEvent(Receiver receiver, Class<? extends Subsystem> subsystemType) implements SystemEvent {
}
