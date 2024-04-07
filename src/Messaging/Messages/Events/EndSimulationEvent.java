package Messaging.Messages.Events;

/**
 * EndSimulationEvent record, models a kill event for this simulation.
 *
 * @param msg A cute message.
 *
 * @author MD & BK
 * @version Iteration-5
 */
public record EndSimulationEvent(String msg) implements SystemEvent {}