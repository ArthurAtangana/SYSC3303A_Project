package Messaging.Transceivers.Receivers;

/**
 * UDP receiver proxy, defines method to get port without having to initialize a receiver which can receive messages.
 */
public abstract class ReceiverUDPProxy extends Receiver {

    protected ReceiverUDPProxy(int key) {
        super(key);
    }

    /**
     * Get the port this receiver's socket is receiving on.
     *
     * @return The port this receiver is receiving on.
     */
    abstract public int getPort();
}