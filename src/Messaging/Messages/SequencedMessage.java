package Messaging.Messages;

/**
 * Record which wraps a system message with an associated sequence key to identify them for the request/reply protocol.
 * @param sequenceKey The sequence number of the message sent
 * @param message The message to be sent.
 *
 * @author Alex
 * @version iteration-3
 */
public record SequencedMessage(int sequenceKey, SystemMessage message) implements SystemMessage{ }
