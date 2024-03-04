package Messaging.Transceivers;

import Messaging.Transceivers.Receivers.Receiver;
import Messaging.Transceivers.Receivers.ReceiverUDP;
import Messaging.Transceivers.Transmitters.Transmitter;
import Messaging.Transceivers.Transmitters.TransmitterUDP;

public class TransceiverUDPFactory implements TransceiverFactory {
    // Define server_receiver as a singleton
    private static final ReceiverUDP server_receiver = new ReceiverUDP(0, 8008);

    /**
     * Receiver for the server, receives the client events.
     *
     * @return The server's receiver instance.
     */
    @Override
    public Receiver createServerReceiver() {
        return server_receiver;
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
    public Transmitter<ReceiverUDP> createServerTransmitter() {
        return new TransmitterUDP();
    }

    /**
     * Transmitter for the client, sends client events to the server.
     * Transmitter is bound to server on creation.
     *
     * @return A client transmitter instance.
     */
    @Override
    public Transmitter<ReceiverUDP> createClientTransmitter() {
        TransmitterUDP clientTransmitter = new TransmitterUDP();
        // Bind client to server
        clientTransmitter.addReceiver(server_receiver);
        return clientTransmitter;
    }
}
