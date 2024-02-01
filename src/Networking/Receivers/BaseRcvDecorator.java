package Networking.Receivers;

import Networking.Events.ElevatorSystemEvent;

public abstract class BaseRcvDecorator implements Receiver {
    final private Receiver receiver;

    protected BaseRcvDecorator(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public ElevatorSystemEvent receive() {
        return receiver.receive();
    }
}
