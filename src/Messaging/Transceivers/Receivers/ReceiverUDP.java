package Messaging.Transceivers.Receivers;

import Messaging.Messages.SystemMessage;
import Messaging.Transceivers.TransceiverUtility;

import java.io.*;
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
    private byte[] responseMessage;

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
        responseMessage = new byte[]{(byte) key, 2, 3, 4};
    }

    /**
     * Initializes a UDP DatagramSocket that binds on any available port (wildcard).
     */
    public ReceiverUDP(int key) {
        super(key);
        try {
            this.receiveSocket = new DatagramSocket();
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
        SystemMessage deserializedMessage = TransceiverUtility.deserializeSystemMessage(packet.getData());
        enqueueMessage(deserializedMessage);

        sendReply(packet);
    }

    /**
     * Creates a temporary DatagramSocket and sends a response packet to the transmitter.
     *
     * @param packet Packet that was received from the receiveSocket.
     */
    private void sendReply(DatagramPacket packet) {
        DatagramPacket sendPacket = new DatagramPacket(responseMessage, responseMessage.length,
                packet.getAddress(), TransceiverUtility.REPLY_PORT);

        DatagramSocket tempSocket = null;
        try {
            tempSocket = new DatagramSocket();
        } catch (SocketException se) {
            se.printStackTrace();
            System.exit(1);
        }
        try {
            tempSocket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        tempSocket.close();
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
