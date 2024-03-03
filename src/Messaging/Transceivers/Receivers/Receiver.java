package Messaging.Transceivers.Receivers;

import Messaging.Messages.SystemMessage;


import java.util.LinkedList;

public abstract class Receiver {
    // Message buffer TODO: replace w/ priority queue class (prioritizes based on message class)
    protected final LinkedList<SystemMessage> msgBuf;
    protected final int key;

    protected Receiver(int key) {
        this.key = key;
        this.msgBuf = new LinkedList<>();
    }

    /**
     * Receives an event from a Transmitter.
     * Blocks if no event has arrived yet.
     *
     * @return A message sent from a transmitter.
     */
    public abstract SystemMessage receive();

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
        }

        return msgBuf.removeFirst();
    }
}
