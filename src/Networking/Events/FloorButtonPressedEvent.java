package Networking.Events;

/**
 * Event generated when Floor button pressed by passenger
 * @param data All passenger data
 */
public record FloorButtonPressedEvent(Passenger data) implements ElevatorSystemEvent{
}
