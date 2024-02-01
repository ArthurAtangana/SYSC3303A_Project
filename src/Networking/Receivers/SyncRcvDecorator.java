package Networking.Receivers;

import Networking.Events.ElevatorSystemEvent;

public class SyncRcvDecorator extends BaseRcvDecorator {
    SyncRcvDecorator(Receiver receiver) {
        super(receiver);
    }

    @Override
    synchronized public ElevatorSystemEvent receive() {
        ElevatorSystemEvent msg = super.receive();
        while(msg == null){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            msg = super.receive();
        }
        return msg;
    }

}
