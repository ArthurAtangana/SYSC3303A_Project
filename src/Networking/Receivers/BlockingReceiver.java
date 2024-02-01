package Networking.Receivers;

import Networking.Events.ElevatorSystemEvent;

public interface BlockingReceiver {
    // Null msg = discard msg
    ElevatorSystemEvent blockingReceive();

    void notifyMsgReceived();
}
