package Messaging.Messages.Events;

import Messaging.Transceivers.Receivers.Receiver;

public record ReceiverBindingEvent(Receiver receiver, Class<?> subsystemType) implements SystemEvent {
}
