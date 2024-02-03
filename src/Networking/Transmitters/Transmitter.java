package Networking.Transmitters;

import Networking.Events.ElevatorSystemEvent;

public interface Transmitter {
    void send(ElevatorSystemEvent event);
}
