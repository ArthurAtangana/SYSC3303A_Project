package Messaging.Messages.Events;

/**
 * StartSimulationEvent record, models an event to signal the simulation has started.
 *
 * @param msg A cute message.
 *
 * @author Snake Plissken
 * @version Iteration-5
 */
public record StartSimulationEvent(String msg) implements SystemEvent {}