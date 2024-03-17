package Mains;

import Configuration.Config;
import Configuration.Configurator;
import Messaging.Transceivers.TransceiverFactory;
import Messaging.Transceivers.TransceiverUDPFactory;
import Subsystem.SchedulerSubsystem.Scheduler;

public class MainSchedulerUDP {
    /**
     * UDP Scheduler start procedure:
     * 1. Create transceiver factory
     * 2. Create and start scheduler
     */
    public static void main(String[] args) {
        // 1. Create transceiver factory
        TransceiverFactory transceiverFactory = new TransceiverUDPFactory();

        // 2. Get Config
        Config config = (new Configurator().getConfig());

        // 3. Create and start scheduler
        Scheduler scheduler = new Scheduler(config,
                transceiverFactory.createServerReceiver(),
                transceiverFactory.createServerTransmitter(),
                transceiverFactory.createServerTransmitter());
        new Thread(scheduler).start();
    }
}
