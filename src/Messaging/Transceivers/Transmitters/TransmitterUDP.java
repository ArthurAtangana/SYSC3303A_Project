package Messaging.Transceivers.Transmitters;

import Messaging.Messages.AcknowledgementMessage;
import Messaging.Messages.SequencedMessage;
import Messaging.Messages.SerializationHelper;
import Messaging.Messages.SystemMessage;
import Messaging.Transceivers.Receivers.ReceiverUDPProxy;

import java.io.IOException;
import java.net.*;

/**
 * TransmitterUDP class, provides a way to send messages to UDP_Receivers.
 *
 * @version Iteration-3
 */
public class TransmitterUDP extends Transmitter<ReceiverUDPProxy> {
    private static int messageSequenceNum = 0; // Increments after each message sent
    private static final int TIMEOUT = 100;
    private static final int MAX_TIMEOUTS = 3;


    private final DatagramSocket sendSocket;

    /**
     * Initializes a UDP DatagramSocket that will send SystemMessages to its receivers.
     */
    public TransmitterUDP() {
        // IMPORTANT: The class written here has to be the same as the concrete generic in the class definition "<>"
        super(ReceiverUDPProxy.class);
        // Initialize socket to send messages on
        try {
            this.sendSocket = new DatagramSocket();
            sendSocket.setSoTimeout(TIMEOUT);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends a serialized SystemMessage from the sendSocket.
     *
     * @param message The SystemMessage to be sent to receivers.
     */
    @Override
    public void send(SystemMessage message) {
        // Send the message to each receiver bound to this transmitter, wrapped with the message sequence number
        byte[] msg = SerializationHelper.serializeSystemMessage(new SequencedMessage(messageSequenceNum++, message));

        for (ReceiverUDPProxy rx : receivers) {
            udpSingleSendReceive(rx, msg);
        }
    }

    /**
     * Sends (and waits for acknowledgement) to a single given receiver.
     * @param rx The receiver to send to.
     * @param msg The message to send (as a byte array).
     */
    private void udpSingleSendReceive(ReceiverUDPProxy rx, byte[] msg)  {
        boolean isAcknowledged = false;
        int consecutiveTimeouts = 0;
        while(!isAcknowledged && consecutiveTimeouts < MAX_TIMEOUTS){
            udpSingleSend(rx, msg);
            isAcknowledged = udpSingleReceive();
            consecutiveTimeouts++;
        }
        if (consecutiveTimeouts == MAX_TIMEOUTS){
            throw new RuntimeException("MAX_TIMEOUTS reached!");
        }
        System.out.println("DEBUG: Message acknowledgement received in transmitter");
    }

    private void udpSingleSend(ReceiverUDPProxy rx, byte[] msg){
        try {
//                System.out.println("DEBUG: sending on localhost, port"+ rx.getPort());
            DatagramPacket packet = new DatagramPacket(msg, msg.length, InetAddress.getLocalHost(), rx.getPort());
            sendSocket.send(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean udpSingleReceive(){
        byte[] rcvBuf = new byte[AcknowledgementMessage.ACK_LEN];
        try {
            sendSocket.receive(new DatagramPacket(rcvBuf, rcvBuf.length));
            // Because request/reply is fully synced (at the moment), receiving the message alone should acknowledge it.
            return true; // If this line is reached, message was acknowledged.
        } catch (SocketTimeoutException e){
            System.out.println("DEBUG: Timed out, retrying.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Message not received
        return false;
    }
}
