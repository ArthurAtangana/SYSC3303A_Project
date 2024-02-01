package Networking.Transmitters;

import Networking.Events.ElevatorSystemEvent;

public abstract class BaseSndDecorator implements Transmitter {
    final protected Transmitter transmitter;

    protected BaseSndDecorator(Transmitter transmitter) {
        this.transmitter = transmitter;
    }

    @Override
    public void send(ElevatorSystemEvent event) {
        transmitter.send(event);
    }
}
