package Messaging.Transceivers;

import Messaging.Transceivers.Receivers.Receiver;
import Messaging.Transceivers.Receivers.ReceiverUDP;
import Messaging.Transceivers.Receivers.ReceiverUDPProxy;
import Messaging.Transceivers.Receivers.ReceiverUDPStub;
import Messaging.Transceivers.Transmitters.TransmitterUDP;

public class TransceiverUDPFactory implements TransceiverFactory {
    // Singleton proxy for server
    private static final ReceiverUDPProxy SERVER_PROXY = new ReceiverUDPStub(8008);

    /**
     * Receiver for the server, receives the client events.
     *
     * @return The server's receiver instance.
     */
    @Override
    public Receiver createServerReceiver() {
        // Will raise bind exception when trying to instantiate more than one server at a time
        ReceiverUDP serverReceiver = new ReceiverUDP(0, SERVER_PROXY.getPort());
        new Thread(serverReceiver).start(); // Activate receiver
        return serverReceiver;
    }

    /**
     * Receiver for the client, receives server events.
     *
     * @param key key identifying the client instance for command events.
     * @return A client receiver instance.
     */
    @Override
    public Receiver createClientReceiver(int key) {
        ReceiverUDP clientReceiver = new ReceiverUDP(key);
        new Thread(clientReceiver).start(); // Activate receiver
        return clientReceiver;
    }

    /**
     * Transmitter for the server, sends server events to the clients.
     * Needs to be bound to clients as messages come in.
     *
     * @return A server transmitter instance.
     */
    @Override
    public TransmitterUDP createServerTransmitter() {
        return new TransmitterUDP();
    }

    /**
     * Transmitter for the client, sends client events to the server.
     * Transmitter is bound to server on creation.
     *
     * @return A client transmitter instance.
     */
    @Override
    public TransmitterUDP createClientTransmitter() {
        TransmitterUDP clientTransmitter = new TransmitterUDP();
        // Bind client to server
        clientTransmitter.addReceiver(SERVER_PROXY.getSerializableReceiver());
        return clientTransmitter;
    }
}