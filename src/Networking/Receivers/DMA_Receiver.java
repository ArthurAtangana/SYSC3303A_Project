package Networking.Receivers;

import Networking.Events.ElevatorSystemEvent;

import java.util.ArrayList;

// TODO: Docs
public class DMA_Receiver implements Receiver{
    private final ArrayList<ElevatorSystemEvent> msgBuf;
    public DMA_Receiver(){
        msgBuf = new ArrayList<>();
    }
    @Override
    public synchronized ElevatorSystemEvent receive() {
        while(msgBuf.isEmpty()){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return msgBuf.remove(0);
    }

    // DMA Receiver asynch receiving method
    public synchronized void setMessage(ElevatorSystemEvent msg){
        msgBuf.add(msg);
        notifyAll();
    }
}
