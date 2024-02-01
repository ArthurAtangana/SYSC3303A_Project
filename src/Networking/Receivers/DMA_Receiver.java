package Networking.Receivers;

import Networking.Events.ElevatorSystemEvent;

import java.util.ArrayList;

// TODO: Docs
public class DMA_Receiver implements Receiver{
    private final ArrayList<ElevatorSystemEvent> msgBuf;
    private final Class<ElevatorSystemEvent> eventFilter;

    DMA_Receiver(Class<ElevatorSystemEvent> eventFilter){
        this.eventFilter = eventFilter;
        msgBuf = new ArrayList<>();
    }

    private synchronized ElevatorSystemEvent consumeMsg() {
        return msgBuf.isEmpty() ? null : msgBuf.remove(0);
    }

    @Override
    public synchronized ElevatorSystemEvent receive() {
        ElevatorSystemEvent msg = consumeMsg();
        while(msg == null){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // Notified, test again
            msg = consumeMsg();
        }
        return msg;
    }

    @Override
    public synchronized void notifyMsgReceived() {
        // TODO: May need a guard to NOOP if nothing is waiting here
        notifyAll();
    }

    // DMA Receiver asynch receiving method
    public void setMessage(ElevatorSystemEvent msg){
        if (eventFilter.isInstance(msg)) {
            msgBuf.add(msg);
        }
    }
}
