package Networking.Receivers;

import Networking.Events.ElevatorSystemEvent;

// TODO: Docs
public class DMA_Receiver implements Receiver, BlockingReceiver{
    private ElevatorSystemEvent msgBuf;
    private final Class<ElevatorSystemEvent> eventFilter;

    DMA_Receiver(Class<ElevatorSystemEvent> eventFilter){
        this.eventFilter = eventFilter;
        msgBuf = null;
    }

    @Override
    public synchronized ElevatorSystemEvent receive() {
        // TODO: May want to raise errors when overriding, or implement a bigger buffer
        ElevatorSystemEvent msg = msgBuf;
        msgBuf = null;
        return msg;
    }

    @Override
    public synchronized ElevatorSystemEvent blockingReceive() {
        ElevatorSystemEvent msg = receive();
        while(msg == null){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // Notified, test again
            msg = receive();
        }
        return msg;
    }

    @Override
    public void notifyMsgReceived() {
        // TODO: May need a guard to NOOP if nothing is waiting here
        notify();
    }

    // DMA Receiver asynch receiving method
    public synchronized void setMessage(ElevatorSystemEvent msg){
        if (eventFilter.isInstance(msg)) {
            msgBuf = msg;
        }
    }
}
