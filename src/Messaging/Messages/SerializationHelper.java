package Messaging.Messages;

import java.io.*;

/**
 * Contains static serialization methods for system messages.
 *
 * @author Victoria
 * @version Iteration-3
 */
public class SerializationHelper {
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
        }
        catch (EOFException e) {
            System.out.println("DEBUG: EOF error likely due to a small receiver buffer");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return deserializedMessage;
    }
}
