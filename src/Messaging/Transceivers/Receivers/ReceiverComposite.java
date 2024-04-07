package Messaging.Transceivers.Receivers;

/**
 * Groups the output of all receivers by dequeueing all receivers as soon as possible into its own queue.
 */
public class ReceiverComposite extends Receiver {
    public ReceiverComposite(int key) {
        super(key);
    }

    /**
     * Returns serializable version of this receiver.
     * For composite, should not serialize.
     * @return The serializable equivalent to this receiver instance.
     */
    @Override
    public SerializableReceiver getSerializableReceiver() {
        // This receiver type is meant to be local to a system at the moment.
        throw new RuntimeException("Composite receiver should not be serialized/passed to other systems");
    }

    /**
     * Claims receiver by taking all of its input into the ReceiverComposite queue.
     * Does not take care of starting/receiving with the receiver, needs to work independently.
     *
     * @param receiver The receiver to claim by the composite.
     */
    public void claimReceiver(Receiver receiver) {
        new Thread(() -> {
            while (true) {
                enqueueMessage(receiver.dequeueMessage());
            }
        }).start();
    }
}