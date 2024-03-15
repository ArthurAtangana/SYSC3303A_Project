package Messaging.Transceivers;

import Messaging.Messages.SystemMessage;

import java.io.*;

/**
 *  Contains static methods used by Receivers and Transmitters.
 *
 * @version Iteration-3
 */
public class TransceiverUtility {

    public static final int REPLY_PORT = 23;

    /**
     * Deserializes a SystemMessage from a DatagramPacket's data.
     *
     * @param message DatagramPacket byte array.
     * @return SystemMessage object.
     */
    public static SystemMessage deserializeSystemMessage(byte[] message) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(message);
        SystemMessage deserializedMessage = null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            deserializedMessage = (SystemMessage) objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } // catch (EOFException) TODO: may need this
        return deserializedMessage;
    }

    /**
     * Serializes a SystemMessage into a byte array.
     *
     * @param message SystemMessage to be serialized into a byte array.
     * @return byte array of serialized message.
     */
    public static byte[] serializeSystemMessage(SystemMessage message) {
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
}
