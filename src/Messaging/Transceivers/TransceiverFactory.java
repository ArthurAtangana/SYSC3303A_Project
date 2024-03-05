package Messaging.Transceivers;

import Messaging.Transceivers.Receivers.Receiver;
import Messaging.Transceivers.Transmitters.Transmitter;


public interface TransceiverFactory {
    /**
     * Receiver for the server, receives the client events.
     *
     * @return The server's receiver instance.
     */
    Receiver createServerReceiver();

    /**
     * Receiver for the client, receives server events.
     *
     * @param key key identifying the client instance for command events.
     * @return A client receiver instance.
     */
    Receiver createClientReceiver(int key);


    /**
     * Transmitter for the server, sends server events to the clients.
     * Needs to be bound to clients as messages come in.
     *
     * @return A server transmitter instance.
     */
    Transmitter<? extends Receiver> createServerTransmitter();

    /**
     * Transmitter for the client, sends client events to the server.
     * Transmitter is bound to server on creation.
     *
     * @return A client transmitter instance.
     */
    Transmitter<? extends Receiver> createClientTransmitter();
}
