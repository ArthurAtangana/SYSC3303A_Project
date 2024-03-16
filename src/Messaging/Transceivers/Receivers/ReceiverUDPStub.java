package Messaging.Transceivers.Receivers;

/**
 * The stub class for the real udp receiver. Stores the port it refers to where the real receiver is listening.
 *
 * @author Alex
 */
public class ReceiverUDPStub extends ReceiverUDPProxy {
    final int port;

    /**
     * Default constructor, allows to specify the port of the stubbed receiver.
     * @param port The port the stub is referring to.
     */
    public ReceiverUDPStub(int port){
        super(0); // Key does not matter since the stub does not actually enqueue messages
        this.port = port;
    }

    /**
     * Get the port this receiver's socket is receiving on.
     *
     * @return The port this receiver is receiving on.
     */
    @Override
    public int getPort() {
        return port;
    }
}
