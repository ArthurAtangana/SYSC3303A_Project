package Networking.Receivers;

import Networking.Events.ElevatorSystemEvent;

public interface Receiver {
    // Null msg = discard msg
    ElevatorSystemEvent receive();

    void notifyMsgReceived();
}
