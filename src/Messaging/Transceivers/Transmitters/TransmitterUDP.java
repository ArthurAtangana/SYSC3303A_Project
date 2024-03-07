package Messaging.Transceivers.Transmitters;

import Messaging.Messages.SystemMessage;
import Messaging.Transceivers.Receivers.ReceiverUDP;
import Messaging.Transceivers.TransceiverUtility;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.net.*;

/**
 * TransmitterUDP class, provides a way to send messages to UDP_Receivers.
 *
 * @version Iteration-3
 */
public class TransmitterUDP extends Transmitter<ReceiverUDP> {
    private final DatagramSocket sendSocket, replyReceiverSocket;
    private final ArrayList<Integer> receiverKeys;

    /**
     * Initializes a UDP DatagramSocket that will send SystemMessages to its receivers.
     */
    // TODO: Late bind, instead of bind on port (make use of super class)
    public TransmitterUDP() {
        // IMPORTANT: The class written here has to be the same as the concrete generic in the class definition "<>"
        super(ReceiverUDP.class);
        this.receiverKeys = new ArrayList<>();
        // Initialize socket to send messages on
        try {
            this.sendSocket = new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        // Initialize socket to receive reply messages on
        try {
            this.replyReceiverSocket = new DatagramSocket(TransceiverUtility.REPLY_PORT);
            replyReceiverSocket.setSoTimeout(1000);
        } catch (SocketException se) {
            throw new RuntimeException(se);
        }
    }

    /**
     * Sends a serialized SystemMessage from the sendSocket.
     *
     * @param message The SystemMessage to be sent to receivers.
     */
    @Override
    public void send(SystemMessage message) {
        // Try to send this message to each receiver bound to this transmitter
        byte[] msg = TransceiverUtility.serializeSystemMessage(message);

        for (ReceiverUDP rx : receivers) {
            try {
                System.out.println(rx.getPort());
                DatagramPacket packet = new DatagramPacket(msg, msg.length, InetAddress.getLocalHost(), rx.getPort());
                sendSocket.send(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        receiveReplies();
    }

    /**
     * Stores the destReceiver key into the receiverKeys array.
     *
     * @param destReceiver The receiver to bind to this transmitter.
     */
    @Override
    public void addReceiver(ReceiverUDP destReceiver) {
        super.addReceiver(destReceiver);
        receiverKeys.add(destReceiver.getKey());
    }

    private void receiveReplies() {
        ArrayList<Integer> receiverFlags = receiverKeys;
        System.out.println("receiverFlags: "  + receiverFlags);

        for (int i = 0; i< receivers.size(); i++) {
            byte[] data = new byte[4];
            DatagramPacket receivePacket = new DatagramPacket(data, data.length);
            try {
                replyReceiverSocket.receive(receivePacket);
                System.out.println("PACKET RECEIVED!");
            } catch (SocketTimeoutException e) {
                System.out.println("No activity after 2 second...");
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
            byte[] replyMessage = receivePacket.getData();
            for (int a = 0; a < replyMessage.length; a++) {
                System.out.print(data[a] + " ");
            }
            System.out.println();

            int receiverKey = replyMessage[i];
            if (receiverFlags.contains(receiverKey)) {
                int index = receiverFlags.indexOf(receiverKey);
                receiverFlags.set(index, -1);
            }
        }

        for (int i = 0; i < receiverFlags.size(); i++) {
            if (receiverFlags.get(i) != -1) {
                System.out.println("Did not receive a reply message from " + receiverFlags.get(i));
            }
        }

        // #TODO resend to the receivers that haven't replied
    }
}
