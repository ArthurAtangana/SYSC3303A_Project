package Messaging.Transceivers.Transmitters;

import Messaging.Messages.SystemMessage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;

/**
 * UDP_Transmitter class, provides a way to send messages to UDP_Receivers.
 *
 * @version Iteration-3
 */
public class UDP_Transmitter extends Transmitter {
    private final DatagramSocket sendSocket;
    private final int sendPort;

    // private final UDP_Receiver replyReceiver;

    /**
     * Initializes a UDP DatagramSocket that will send SystemMessages to the specified port.
     *
     * @param sendPort Port number the UDP_transmitter is sending a DatagramPacket to.
     */
    public UDP_Transmitter(int sendPort) {
        super();
        this.sendPort = sendPort;
        try {
            this.sendSocket = new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        // replyReceiver = new UDP_Receiver(sendSocket.getPort());
    }

    /**
     * Sends a serialized SystemMessage from the sendSocket.
     *
     * @param message The SystemMessage to be sent to receivers.
     */
    @Override
    public void send(SystemMessage message) {
        DatagramPacket packet = null;
        try {
            byte[] msg = serializeSystemMessage(message);
            packet = new DatagramPacket(msg, msg.length, InetAddress.getLocalHost(), sendPort);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        try {
            sendSocket.send(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Serializes a SystemMessage into a byte array.
     *
     * @param message SystemMessage to be serialized into a byte array.
     * @return byte array of serialized message.
     */
    private byte[] serializeSystemMessage(SystemMessage message) {
        ByteArrayOutputStream packet = new ByteArrayOutputStream();
        byte[] serializedData = new byte[0];
        try {
            ObjectOutputStream object = new ObjectOutputStream(packet);
            object.writeObject(message);
            object.close();
            serializedData = packet.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serializedData;
    }

    public SystemMessage receiveReply(){
        // #TODO
        // return replyReceiver.receive();
        return null;
    }
}
