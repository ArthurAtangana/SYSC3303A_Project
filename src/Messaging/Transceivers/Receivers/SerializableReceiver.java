package Messaging.Transceivers.Receivers;

import java.io.Serializable;

/**
 * Interface to label which receivers are serializable, which are passed through ReceiverBindingEvent messages.
 */
public interface SerializableReceiver extends Serializable {
    /**
     * Returns the unique key identifier of this Receiver.
     *
     * @return int key of receiver.
     */
    int getKey();
}
