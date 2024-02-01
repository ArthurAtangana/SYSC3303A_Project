package Networking.Receivers;

import Networking.Events.ElevatorSystemEvent;

public class DMA_Receiver implements Receiver {
    private ElevatorSystemEvent msgBuf;

    @Override
    public ElevatorSystemEvent receive() {
        return msgBuf;
    }

    public void setMessage(ElevatorSystemEvent msg){
        msgBuf = msg;
    }
}
