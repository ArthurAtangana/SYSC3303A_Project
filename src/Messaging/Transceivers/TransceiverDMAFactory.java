package Messaging.Transceivers;

import Messaging.Transceivers.Receivers.Receiver;
import Messaging.Transceivers.Receivers.ReceiverDMA;
import Messaging.Transceivers.Transmitters.Transmitter;
import Messaging.Transceivers.Transmitters.TransmitterDMA;

public class TransceiverDMAFactory implements TransceiverFactory {
    // Define server_receiver as a singleton
    private static final ReceiverDMA serverReceiver = new ReceiverDMA(0);

    /**
     * Receiver for the server, receives the client events.
     *
     * @return The server's receiver instance.
     */
    @Override
    public Receiver createServerReceiver() {
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
        return new ReceiverDMA(key);
    }

    /**
     * Transmitter for the server, sends server events to the clients.
     *
     * @return A server transmitter instance.
     */
    @Override
    public Transmitter<ReceiverDMA> createServerTransmitter() {
        return new TransmitterDMA();
    }

    /**
     * Transmitter for the client, sends client events to the server.
     * Transmitter is bound to server on creation.
     *
     * @return A client transmitter instance.
     */
    @Override
    public Transmitter<ReceiverDMA> createClientTransmitter() {
        TransmitterDMA clientTransmitter = new TransmitterDMA();
        // Bind client to server
        clientTransmitter.addReceiver(serverReceiver);
        return clientTransmitter;
    }
}
