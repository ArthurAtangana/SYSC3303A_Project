package Messaging.Messages;

/**
 * Record message to identify which message was acknowledged (and which receiver received it)
 * @param sequenceKey The sequence number of the message acknowledged.
 * @param receiverKey The receiver which acknowledged the message.
 *
 * @author Alex
 * @version iteration-3
 */
public record AcknowledgementMessage(int sequenceKey, int receiverKey) { }
