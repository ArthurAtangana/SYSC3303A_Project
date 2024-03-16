package Messaging.Transceivers.Transmitters;

import Messaging.Messages.SerializationHelper;
import Messaging.Messages.SystemMessage;
import Messaging.Transceivers.Receivers.ReceiverUDPProxy;
import Messaging.Transceivers.Receivers.SerializableReceiver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * TransmitterUDP class, provides a way to send messages to UDP_Receivers.
 *
 * @version Iteration-3
 */
public class TransmitterUDP extends Transmitter<ReceiverUDPProxy> {
    private final DatagramSocket sendSocket;
    private final ArrayList<Integer> receiverKeys; // Identify "servers"

    /**
     * Initializes a UDP DatagramSocket that will send SystemMessages to its receivers.
     */
    public TransmitterUDP() {
        // IMPORTANT: The class written here has to be the same as the concrete generic in the class definition "<>"
        super(ReceiverUDPProxy.class);
        this.receiverKeys = new ArrayList<>();
        // Initialize socket to send messages on
        try {
            this.sendSocket = new DatagramSocket();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * When adding a receiver, also stores their key
     *
     * @param receiver The receiver to bind to this transmitter.
     */
    @Override
    public void addReceiver(SerializableReceiver receiver) {
        super.addReceiver(receiver);
        receiverKeys.add(receiver.getKey());
    }

    /**
     * Sends a serialized SystemMessage from the sendSocket.
     *
     * @param message The SystemMessage to be sent to receivers.
     */
    @Override
    public void send(SystemMessage message) {
        // Try to send this message to each receiver bound to this transmitter
        byte[] msg = SerializationHelper.serializeSystemMessage(message);

        for (ReceiverUDPProxy rx : receivers) {
            try {
//                System.out.println("DEBUG: sending on localhost, port"+ rx.getPort());
                DatagramPacket packet = new DatagramPacket(msg, msg.length, InetAddress.getLocalHost(), rx.getPort());
                sendSocket.send(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public SystemMessage receiveReply(){
        // #TODO
        // return replyReceiver.receive();
        return null;
    }
}
