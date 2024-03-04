package Messaging.Transceivers.Receivers;

import Messaging.Messages.SystemMessage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * ReceiverUDP class, provides a way to receive messages from UDP_Transmitters.
 *
 * @version Iteration-3
 */
public class ReceiverUDP extends Receiver implements Runnable {
    public final int MAX_MSG_SIZE = 255;
    private final DatagramSocket receiveSocket;

    /**
     * Initializes a UDP DatagramSocket that listens on the specified port.
     *
     * @param receivePort Port number to listen on.
     */
    public ReceiverUDP(int key, int receivePort) {
        super(key);
        try {
            this.receiveSocket = new DatagramSocket(receivePort);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the port this receiver's socket is receiving on.
     *
     * @return The port this receiver is receiving on.
     */
    public int getPort() {
        return receiveSocket.getLocalPort();
    }

    /**
     * Calls receive from the receiveSocket and adds the deserializedMessage to msgBuf.
     */
    private void receiveUDP() {
        byte[] dataBuf = new byte[MAX_MSG_SIZE];
        DatagramPacket packet = new DatagramPacket(dataBuf, dataBuf.length);

        try {
            receiveSocket.receive(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        SystemMessage deserializedMessage = deserializeSystemMessage(packet.getData());

        enqueueMessage(deserializedMessage);
    }

    /**
     * Deserializes a SystemMessage from a DatagramPacket's data.
     *
     * @param message DatagramPacket byte array.
     * @return SystemMessage object.
     */
    private SystemMessage deserializeSystemMessage(byte[] message) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(message);
        SystemMessage deserializedMessage = null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            deserializedMessage = (SystemMessage) objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return deserializedMessage;
    }

    /**
     * Receives SystemMessages and stores them in the msgBuf.
     */
    @Override
    public void run() {
        while (true) {
            receiveUDP();
        }
    }
}
