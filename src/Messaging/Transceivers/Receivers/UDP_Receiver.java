package Messaging.Transceivers.Receivers;

import Messaging.Messages.SystemMessage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 * UDP_Receiver class, provides a way to receive messages from UDP_Transmitters.
 *
 * @version Iteration-3
 */
public class UDP_Receiver implements Runnable, Receiver {
    private final List<SystemMessage> msgBuf;
    public final int MAX_MSG_SIZE = 255;
    private final DatagramSocket receiveSocket;

    /**
     * Initializes a UDP DatagramSocket that listens on the specified port.
     *
     * @param receivePort Port number to listen on.
     */
    public UDP_Receiver(int receivePort){
        this.msgBuf = new ArrayList<>();
        try {
            this.receiveSocket = new DatagramSocket(receivePort);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Waits until msgBuf is not empty and returns the first SystemMessage stored.
     *
     * @return SystemMessage that has been received.
     */
    @Override
    public SystemMessage receive() {
        synchronized (msgBuf) {
            while (msgBuf.isEmpty()) {
                try {
                    msgBuf.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return msgBuf.remove(0);
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

        synchronized (msgBuf) {
            msgBuf.add(deserializedMessage);
            msgBuf.notifyAll();
        }
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
