package Messaging.Transceivers.Receivers;

import Messaging.Messages.Commands.SystemCommand;
import Messaging.Messages.SystemMessage;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Receiver is an abstract class that provides functionalities for de/queuing received messages.
 *
 * Note: The class is abstract because it has no way to receive as is. (Subclasses provide their respective
 * implementation)
 *
 * Receiver parent has to be serializable because we use the constructor.
 */
public abstract class Receiver implements Serializable {
    // Message buffer TODO: replace w/ priority queue class (prioritizes based on message class)
    private final LinkedList<SystemMessage> msgBuf;
    private final int key;

    protected Receiver(int key) {
        this.key = key;
        this.msgBuf = new LinkedList<>();
    }

    /**
     * Returns serializable version of this receiver.
     * @return The serializable equivalent to this receiver instance.
     */
    public abstract SerializableReceiver getSerializableReceiver();

    /**
     * Returns first event in message queue.
     * Blocks if no event has arrived yet.
     *
     * @return A message received from a transmitter.
     */
    public SystemMessage dequeueMessage(){
        synchronized (msgBuf) {
            while (msgBuf.isEmpty()) {
                try {
                    msgBuf.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            return msgBuf.removeFirst();
        }
    }

    /**
     * Returns the unique key identifier of this Receiver.
     *
     * @return int key of receiver.
     */
    public int getKey() {
        // make a getter function for key
        return key;
    }

    /**
     * Add to msg message queue.
     *
     * @param msg the SystemMessage to add to the message queue.
     */
    protected void enqueueMessage(SystemMessage msg){
        // GUARD: Enqueue message IFF message is for this receiver.
        if (!isMessageForThis(msg))
            return;

        synchronized (msgBuf) {
            msgBuf.add(msg);
            msgBuf.notify(); // Single wait type, single notify
        }
    }

    /**
     * Returns true if the msg is addressed to this receiver, false otherwise.
     * Does key checks for command type messages.
     *
     * @param msg The message to check for validity
     * @return True if the message is for this receiver, false otherwise.
     */
    private boolean isMessageForThis(SystemMessage msg){
        return !(msg instanceof SystemCommand && !((SystemCommand) msg).matchKey(key));
    }
}
