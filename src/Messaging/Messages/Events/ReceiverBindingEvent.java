package Messaging.Messages.Events;

import Messaging.Transceivers.Receivers.Receiver;
import Messaging.Transceivers.Receivers.SerializableReceiver;
import Subsystem.Subsystem;

public record ReceiverBindingEvent(SerializableReceiver receiver, Class<? extends Subsystem> subsystemType) implements SystemEvent {
    /**
     * ReceiverBindingEvent constructor to convert a receiver to its serializable equivalent.
     * @param receiver The serializable receiver.
     * @param subsystemType The subsystem type of the receiver passed.
     */
    public ReceiverBindingEvent(Receiver receiver, Class<? extends Subsystem> subsystemType){
        this(receiver.getSerializableReceiver(), subsystemType);
    }
}
