package Messaging.Messages.Events;

/**
 * endSimulationEvent record, models a kill event for this simulation.
 *
 * @param msg A cute message.
 *
 * @author MD & BK
 * @version Iteration-5
 */
public record endSimulationEvent(String msg) implements SystemEvent {}
